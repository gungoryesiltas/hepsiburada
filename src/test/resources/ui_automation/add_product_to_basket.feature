Feature: Hepsiburada - Add Product To Cart


  @ADD_PRODUCT
  Scenario: Add the most expensive apple 13,2 product to the cart
    * Go to url
    * Hover "ELEKTRONIK_TAB"
    * Hover "SUB_CATEGORY_BILGISAYAR_TABLET"
    * Click "SUB_CATEGORY_TABLET_APPLE"
    * Click "TABLET_SIZE_132"
    * Click the most expensive product
    * Switch new window
    * Get product price from product detail page
    * Click "URUN_DETAY_SEPETE_EKLE_BUTON"
    * Click "URUN_DETAY_SEPETE_GIT_BUTTON"
    * Assert that product detail price and cart product price are same