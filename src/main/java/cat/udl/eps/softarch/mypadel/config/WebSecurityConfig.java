package cat.udl.eps.softarch.mypadel.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

				.antMatchers(HttpMethod.POST, "/admins*/**").hasRole("ADMIN")

				.antMatchers(HttpMethod.POST, "/courts*/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/courts*/**").authenticated()
				.antMatchers(HttpMethod.PUT, "/courts*/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/courts*/**").hasRole("ADMIN")

			    .antMatchers(HttpMethod.POST, "/players*/**").hasRole("ADMIN")
			    .antMatchers(HttpMethod.DELETE, "/players*/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.PUT, "/**/*").authenticated()
                .antMatchers(HttpMethod.POST, "/**/*").authenticated()
                .antMatchers(HttpMethod.DELETE, "/**/*").authenticated()
                .antMatchers(HttpMethod.PATCH, "/**/*").authenticated()

			    .antMatchers(HttpMethod.GET,"/identity").authenticated()
				.antMatchers(HttpMethod.POST, "/reservations*/**").authenticated()


			    .antMatchers(HttpMethod.GET,"/identity").authenticated()

				.antMatchers(HttpMethod.POST, "/reservations/*/court").denyAll()
				.antMatchers(HttpMethod.POST, "/reservations*/**").authenticated()

				.antMatchers(HttpMethod.PUT, "/**/*").authenticated()
				.antMatchers(HttpMethod.POST, "/**/*").authenticated()
				.antMatchers(HttpMethod.DELETE, "/**/*").authenticated()
				.antMatchers(HttpMethod.PATCH, "/**/*").authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic().realmName("MyPadel")
                .and()
                .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
                .csrf().disable();
    }
}
