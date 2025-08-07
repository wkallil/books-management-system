package br.com.wkallil.configs;

public interface TestConfigs {
    int SERVER_PORT = 8888;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN= "Origin";
    String ORIGIN_ALLOWED = "http://localhost:8080";
    String ORIGIN_NOT_ALLOWED = "http://localhost:5000";
}
