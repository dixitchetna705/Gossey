package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Gosssey", appName)
  }

  @Test
  fun `verify share translation dictionary keys`() {
    // English sharing dictionary verification
    val enMap = com.example.ui.screens.translations["English"]
    assertNotNull(enMap)
    assertEquals("Share Post", enMap?.get("SharePostTitle"))
    assertEquals("Sharing to WhatsApp...", enMap?.get("SharedToWhatsApp"))
    assertEquals("Sharing to Facebook...", enMap?.get("SharedToFacebook"))
    assertEquals("Sharing to Instagram...", enMap?.get("SharedToInstagram"))
    assertEquals("Post link copied to clipboard!", enMap?.get("CopiedToClipboard"))
    assertEquals("Direct message to classmates", enMap?.get("ShareToUsers"))

    // Hindi sharing dictionary verification
    val hiMap = com.example.ui.screens.translations["Hindi"]
    assertNotNull(hiMap)
    assertEquals("पोस्ट साझा करें", hiMap?.get("SharePostTitle"))
    assertEquals("व्हाट्सएप पर साझा कर रहे हैं...", hiMap?.get("SharedToWhatsApp"))
    assertEquals("फेसबुक पर साझा कर रहे हैं...", hiMap?.get("SharedToFacebook"))
    assertEquals("इंस्टाग्राम पर साझा कर रहे हैं...", hiMap?.get("SharedToInstagram"))
    assertEquals("पोस्ट लिंक क्लिपबोर्ड पर कॉपी किया गया!", hiMap?.get("CopiedToClipboard"))
    assertEquals("सहपाठियों को सीधा संदेश भेजें", hiMap?.get("ShareToUsers"))
  }
}
