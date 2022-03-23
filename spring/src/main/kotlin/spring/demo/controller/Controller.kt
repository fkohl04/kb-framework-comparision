package spring.demo.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.client.RestTemplate
import spring.demo.client.RandomDataClient
import spring.demo.config.RandomDataServerConfig
import spring.demo.database.RandomsRepository

@Controller
class Controller {

    @Autowired
    private lateinit var randomsRepo: RandomsRepository

    @Autowired
    private lateinit var randomDataClient: RandomDataClient

    @GetMapping("/call-server")
    fun getRandomDataFromServer(): ResponseEntity<String> {
        return ResponseEntity.ok(randomDataClient.getRandomData())
    }

    @GetMapping("/call-database")
    fun getRandomDataFromDatabase(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(randomsRepo.findAll().mapNotNull { it.value?.toString() })
    }
}
