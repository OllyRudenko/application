package ua.ollyrudenko.application.universities.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataBaseConfiguration {

	private String driverClassName;
	private String url;
	private String username;
	private String password;

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Profile(value = "postgresql")
	@Bean
	public String postgresqlDatabaseConnection() {
		return "DB postgreSQL connected";
	}

	@Profile(value = "h2")
	@Bean
	public String h2DatabaseConnection() {
		return "DB H2 connected";
	}
}