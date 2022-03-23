package spring.demo.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import spring.demo.config.RandomDataServerConfig


@Service
class RandomDataClient {

    @Autowired
    private lateinit var randomDataServerConfig: RandomDataServerConfig

    val restTemplate = RestTemplate()

    fun getRandomData() = restTemplate.getForEntity(randomDataServerConfig.url, String::class.java).body
}
