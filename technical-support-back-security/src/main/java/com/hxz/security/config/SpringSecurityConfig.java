package com.hxz.security.config;

import com.hxz.security.authentication.code.ImageCodeValidateFilter;
import com.hxz.security.authentication.mobile.MobileAuthenticationConfig;
import com.hxz.security.authentication.mobile.MobileValidateFilter;
import com.hxz.security.authentication.session.CustomLogoutHandler;
import com.hxz.security.authorize.AuthorizeConfigurerManager;
import com.hxz.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity //开启springsecurity过滤链filter
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder(){
        //明文+随机盐值->加密存储//该加密方式内部已经自带了盐值的加密
        return new BCryptPasswordEncoder();
    }
    @Autowired
    UserDetailsService customUserDetailsService;

    /*
     * 认证管理器
     * 1.认证信息(用户名,密码)
     * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        String password=passwordEncoder().encode("123456");
//        log.info("加密之后的密码"+password);
//
//        auth.inMemoryAuthentication().withUser("hxz")
//                .password(password)//密码必须要加密,否则会报错
//                .authorities("Admin")
        auth.userDetailsService(customUserDetailsService);
    }

    //配置过滤器拦截放行参数
    @Autowired
    private SecurityProperties securityProperties;



    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private ImageCodeValidateFilter imageCodeValidateFilter;

    //校验手机验证码
    @Autowired
    private MobileValidateFilter mobileValidateFilter;

    //校验手机号是否存在
    @Autowired
    private MobileAuthenticationConfig mobileAuthenticationConfig;

    //配置账户登录信息存储时间（多久会失效，需要重新登陆）
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    /*
    * 配置同一个账户同时多个登录在线报错
    * 即当同个用户session数量超过指定值之后，会调用这个实现类
    * */
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    /*
    * 这里故意以接口的形式注入进来。
    * */
    @Autowired
    private AuthorizeConfigurerManager authorizeConfigurerManager;



    /*
    * 记住我登录功能
    * */
    @Autowired
    DataSource dataSource;

    public JdbcTokenRepositoryImpl jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //是否启动项目是自动创建表，true为开启自动创建
        jdbcTokenRepository.setCreateTableOnStartup(false);

        return jdbcTokenRepository;
    }


    /*
    * 资源权限进行配置
    * 1.被拦截的资源
    *
    * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*当springsecurity访问成功之后会返回到你上一次访问的想要进入的页面*/
//        http.httpBasic() //采用httpBasic认证方式

        //将securityProperties.getAuthentication进行定义缩减，方便下面的代码更加简洁清晰
        //AuthenticationProperties authentication=securityProperties.getAuthentication();

        http.addFilterBefore(mobileValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()//表单登录方式,与Basic方式2这选其一
                .loginPage(securityProperties.getAuthentication().getLoginPage()) //设置登录的路径
                .loginProcessingUrl(securityProperties.getAuthentication().getLoginProcessingUrl()) //设置登录表单提交处理的url，默认是/login
                .usernameParameter(securityProperties.getAuthentication().getUsernameParameter())//设置页面输入用户名表单字段值名称,默认是username
                .passwordParameter(securityProperties.getAuthentication().getPasswordParameter())//设置页面输入密码表单字段值名称,默认是password
                .successHandler(customAuthenticationSuccessHandler)//设置认证成功后页面返回JSON消息（与authentication下的CustomAuthenticationSuccessHandler有关）
                .failureHandler(customAuthenticationFailureHandler)//设置认证失败后页面返回JSON消息
//                .and()
//                .authorizeRequests()//认证请求
//                .antMatchers(securityProperties.getAuthentication().getLoginPage(),
//                        securityProperties.getAuthentication().getImageCodeUrl(),
//                        securityProperties.getAuthentication().getMobileCodeUrl(),
//                        securityProperties.getAuthentication().getMobilePage()
//                        ).permitAll()//放行/login/page不需要认证
//                .antMatchers("/user").hasAuthority("sys:user")
//                .antMatchers(HttpMethod.GET,"/role").hasAuthority("sys:role")
//                .antMatchers(HttpMethod.GET,"/permission")
//                .access("hasAnyAuthority('sys:permission') or hasAnyRole('ADMIN','ROOT')")
//                .anyRequest().authenticated()//所有访问该应用的http请求都需要通过身份认证才能访问
                .and()
                .rememberMe()//记住我功能配置
                .tokenRepository(jdbcTokenRepository())  //使用上面写的jdbcTokenRepository来读取存储的token//保存登录信息
                .tokenValiditySeconds(securityProperties.getAuthentication().getTokenValiditySeconds())//配置记住我功能保存账号名称时长为7天，单位为s
                .and()
                .sessionManagement() //session管理
                .invalidSessionStrategy(invalidSessionStrategy) //当session失效后的处理类
                .maximumSessions(1) //每个用户在系统汇总最多可以有多少个session（即一个账号同时登录能存在几个）
                .expiredSessionStrategy(sessionInformationExpiredStrategy) //超过最大值，即同一时间内相同账号多个登录时，调用和这个类
//                .maxSessionsPreventsLogin(true)//当一个用户达到最大session数，则不允许后面再登录//这个功能一般是关闭的，因为这个功能开启对盗账号的人有利
//                .and().and().csrf().disable() //关闭跨站请求伪造
                .sessionRegistry(sessionRegistry()) //为了解决退出重新登录报已登录问题，使用我们自定义的sessRegistry方法
        .and().and()
                .logout()
                .addLogoutHandler(customLogoutHandler)//退出清除唤醒
                .logoutUrl("/user/logout")//配置自定义退出请求路径
                .logoutSuccessUrl("/mobile/page")//配置自定义退出成功后跳转地址
                .deleteCookies("JSESSIONID")//退出后删除你当前配置的cookie值，注意：cookie要与你定义的一致
                ;

        http.csrf().disable(); //关闭跨站请求伪造

        //将手机认真添加到过滤器链上
        http.apply(mobileAuthenticationConfig);

        //将所有的授权配置统一的管理起来
        authorizeConfigurerManager.configure(http.authorizeRequests());

    }


    @Autowired
    private CustomLogoutHandler customLogoutHandler;


    /*
    * 为了解决退出重新登录报账号已登录的问题
    * */
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }




    /*关于静态资源的放行，我们一般不在上面的方法里面写，在下面重新定义*/
    /*下面这个方法一般是争对静态资源放行*/
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(securityProperties.getAuthentication().getStaticPaths());
//        web.ignoring().antMatchers("/**");//这个写法不行，会导致template下原本不放行的html文件也放行
    }
}


