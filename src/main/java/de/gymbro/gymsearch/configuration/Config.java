package de.gymbro.gymsearch.configuration;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class Config {
    public final static String CLIENT_HOST = "mobile-clients-api.rewe.de";
    public final static String API_HOST = "mobile-api.rewe.de";

    @Value("${gymsearch.admin.username}")
    private String adminUsername;

    @Value("${gymsearch.admin.password}")
    private String adminPassword;

    @Value("${gymsearch.keystore}")
    private Resource keyStoreResource;

    @Value("${gymsearch.keystore.password}")
    private String keystorePassword;

    private final List<String> userAgents = List.of(
            "Phone/Samsung_SM-G975U", "Phone/Samsung_SM-N975U", "Phone/Samsung_SM-G973U", "Phone/OnePlus_HD1925",
            "Phone/Xiaomi_M2007J3SY", "Phone/LG_LM-G820", "Phone/Google_Pixel_8_Pro", "Phone/Google_Pixel_7_Pro",
            "Phone/Samsung_SM-S911B", "Phone/Samsung_SM-S918B", "Phone/OnePlus_AC2003", "Phone/Xiaomi_2201123G",
            "Phone/Google_Pixel_8", "Phone/Google_Pixel_7a", "Phone/Samsung_SM-F946B", "Phone/Samsung_SM-S901B"
    );

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails admin = User.withUsername(adminUsername)
                .password(passwordEncoder().encode(adminPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request -> request.anyRequest()
                .authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RestTemplate reweSslRestTemplate() throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (var in = keyStoreResource.getInputStream()) {
            ks.load(in, keystorePassword.toCharArray());
        }

        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(ks, keystorePassword.toCharArray())
                .build();

        TlsSocketStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
                .setSslContext(sslContext)
                .buildClassic();

        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setTlsSocketStrategy(tlsStrategy)
                .build();

        CloseableHttpClient closableHttpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(closableHttpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getInterceptors().add((request, body, execution) -> {
            setHeadersForReweClient(request);
            return execution.execute(request, body);
        });

        return restTemplate;
    }

    private void setHeadersForReweClient(HttpRequest request) {
        String userAgentSuffix = userAgents.get(new Random().nextInt(userAgents.size()));

        HttpHeaders headers = request.getHeaders();
        headers.set("rdfa", UUID.randomUUID().toString());
        headers.set("Correlation-Id", UUID.randomUUID().toString());
        headers.set("rd-service-types", "UNKNOWN");
        headers.set("x-rd-service-types", "UNKNOWN");
        headers.set("rd-is-lsfk", "false");
        headers.set("rd-customer-zip", "");
        headers.set("rd-postcode", "");
        headers.set("x-rd-customer-zip", "");
        headers.set("rd-market-id", "");
        headers.set("x-rd-market-id", "");
        headers.set("a-b-test-groups", "productlist-citrusad");
        headers.set(HttpHeaders.USER_AGENT, String.format("REWE-Mobile-Client/3.18.5.33032 Android/14 %s", userAgentSuffix));
        headers.set(HttpHeaders.HOST, request.getURI().getHost());
        headers.set(HttpHeaders.CONNECTION, "Keep-Alive");
    }
}
