package io.quarkus.grpc.examples.hello;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class HelloWorldEndpointTest {

    @Test
    public void testHelloWorldServiceUsingMutinyStub() throws InterruptedException {
        String response = get("/hello/mutiny/neo-mutiny").asString();
        assertThat(response).startsWith("Hello neo-mutiny");
    }

}
