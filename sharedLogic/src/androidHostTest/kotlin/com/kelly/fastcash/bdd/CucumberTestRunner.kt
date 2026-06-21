package com.kelly.fastcash.bdd

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/commonTest/resources/features"],
    glue = ["com.kelly.fastcash.bdd"],
    plugin = ["pretty", "html:build/reports/cucumber/report.html"]
)
class CucumberTestRunner
