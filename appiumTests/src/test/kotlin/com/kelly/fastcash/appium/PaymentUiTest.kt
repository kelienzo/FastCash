package com.kelly.fastcash.appium

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent
import io.appium.java_client.android.options.UiAutomator2Options
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.URI
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
//            .setDeviceName("Android Device")
            .setDeviceName("10.198.45.243:5555")
            .setAppPackage("com.kelly.fastcash")
            .setAppActivity(".MainActivity")
            .setNoReset(true)
            .setFullReset(false)
            .setAppWaitActivity(".MainActivity")
            .setAppWaitDuration(Duration.ofSeconds(20))

        // Ensure Appium server is running at localhost:4723
        // If running Appium 2.0, the base path is usually "/" (default)
        driver = AndroidDriver(URI("http://10.198.45.1:4723/").toURL(), options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
    }

    @Test
    fun testSendPaymentAndVerifyInHistory() {
        // Automatically launch/activate the app at the start of the test
        driver.activateApp("com.kelly.fastcash")

        // 1. Enter Email
        // Using XPath to target the EditText specifically to avoid InvalidElementStateException
        val emailField =
            driver.findElement(AppiumBy.xpath("//android.widget.EditText[.//android.view.View[@content-desc='email_field']]"))
        emailField.click()
        emailField.clear()
        emailField.sendKeys("test@fastcash.com")

        // 2. Enter Amount
        val amountField =
            driver.findElement(AppiumBy.xpath("//android.widget.EditText[.//android.view.View[@content-desc='amount_field']]"))
        amountField.click()
        amountField.clear()
        amountField.sendKeys("150")

        // Hide keyboard to ensure the dropdown is visible
        try {
            if (driver.isKeyboardShown) {
                driver.pressKey(KeyEvent(AndroidKey.BACK))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 3. Select Currency (Open Dropdown)
        val currencySelector = driver.findElement(AppiumBy.accessibilityId("currency_selector"))
        currencySelector.click()

        // Select "USD" from dropdown
        val usdOption = driver.findElement(AppiumBy.accessibilityId("currency_USD"))
        usdOption.click()

        // 4. Click Send Payment
        val sendButton = driver.findElement(AppiumBy.accessibilityId("send_button"))
        sendButton.click()

        // 5. Verify Success/Failure Dialog
        // Use a more robust selector that finds the text "OK" regardless of the container
        val okButton =
            driver.findElement(AppiumBy.xpath("//*[@text='OK' or @text='Ok' or @text='ok']"))
        okButton.click()

        // 6. Verify item appears in the list (transaction_list test tag)
        // We wait a bit for the list to refresh from Firestore
        Thread.sleep(2000)

        // Check if the recipient email appears in the list
        val result =
            driver.findElements(AppiumBy.xpath("//android.widget.TextView[@text='test@fastcash.com']"))
        assertTrue(
            result.isNotEmpty(),
            "Transaction 'test@fastcash.com' should be visible in the history list"
        )
    }

    @AfterAll
    fun tearDown() {
        if (this::driver.isInitialized) {
            driver.quit()
        }
    }
}
