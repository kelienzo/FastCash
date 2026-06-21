package com.kelly.fastcash.appium

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.URL
import java.time.Duration
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentUiTest {

    private lateinit var driver: AndroidDriver

    @BeforeAll
    fun setUp() {
        val options = UiAutomator2Options()
            .setPlatformName("Android")
            .setAutomationName("UiAutomator2")
            .setDeviceName("Android Device")
            .setAppPackage("com.kelly.fastcash")
            .setAppActivity(".MainActivity")
            .setNoReset(false)
            .setFullReset(false)

        // Ensure Appium server is running at localhost:4723
        // If running Appium 2.0, the base path is usually "/" (default)
        driver = AndroidDriver(URL("http://127.0.0.1:4723"), options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
    }

    @Test
    fun testSendPaymentAndVerifyInHistory() {
        // 1. Enter Email
        val emailField = driver.findElement(AppiumBy.accessibilityId("email_field"))
        emailField.sendKeys("test@fastcash.com")

        // 2. Enter Amount
        val amountField = driver.findElement(AppiumBy.accessibilityId("amount_field"))
        amountField.sendKeys("150")

        // 3. Select Currency (Open Dropdown)
        val currencySelector = driver.findElement(AppiumBy.accessibilityId("currency_selector"))
        currencySelector.click()

        // Select "USD" from dropdown
        val usdOption = driver.findElement(AppiumBy.accessibilityId("currency_USD"))
        usdOption.click()

        // 4. Click Send Payment
        val sendButton = driver.findElement(AppiumBy.accessibilityId("send_button"))
        sendButton.click()

        // 5. Verify Success Dialog
        val okButton = driver.findElement(AppiumBy.xpath("//android.widget.Button[@text='OK']"))
        okButton.click()

        // 6. Verify item appears in the list (transaction_list test tag)
        val transactionList = driver.findElement(AppiumBy.accessibilityId("transaction_list"))

        // Check if the recipient email appears in the list
        val result =
            driver.findElements(AppiumBy.xpath("//android.widget.TextView[@text='test@fastcash.com']"))
        assertTrue(result.isNotEmpty(), "Transaction should be visible in the history list")
    }

    @AfterAll
    fun tearDown() {
        if (this::driver.isInitialized) {
            driver.quit()
        }
    }
}
