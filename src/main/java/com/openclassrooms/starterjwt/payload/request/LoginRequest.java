package com.openclassrooms.starterjwt.payload.request;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class LoginRequest {
	@NotBlank
  private String email;

	@NotBlank
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LoginRequest)) return false;
		LoginRequest that = (LoginRequest) o;
		return Objects.equals(email, that.email) &&
				Objects.equals(password, that.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, password);
	}

	@Override
	public String toString() {
		return "LoginRequest{" +
				"email='" + email + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
