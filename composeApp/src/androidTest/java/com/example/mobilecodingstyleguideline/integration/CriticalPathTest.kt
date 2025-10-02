package com.example.mobilecodingstyleguideline.integration

import android.util.Log
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEditable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobilecodingstyleguideline.KoinTestRule
import com.example.mobilecodingstyleguideline.MainActivity
import com.example.mobilecodingstyleguideline.productionModule
import com.example.mobilecodingstyleguideline.testAppModule
import com.example.shared_test.data.source.network.datasource.FakeSupplierApiDataSource
import com.example.shared_test.util.DataDummy.INITIAL_SUPPLIER_DTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.assertEquals

/**
 * Full end-to-end critical path test using KoinTestRule for proper test isolation.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CriticalPathTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule(
        modules = listOf(productionModule, testAppModule)
    )

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var fakeApiDataSource: FakeSupplierApiDataSource

    @Before
    fun setup() {
        fakeApiDataSource = get()
    }

    @Test
    fun createSupplier_addNewItemToList() {
        // --- ACT & ASSERT ---

        // Assert, has entered "Supplier" Main Menu
        composeTestRule.onNodeWithText("Supplier").assertIsDisplayed()

        // Assert that existing data is displayed
        composeTestRule.onNodeWithText(INITIAL_SUPPLIER_DTO.first().companyName)
            .assertIsDisplayed()

        // Perform Create FAB Click
        composeTestRule.onNodeWithTag("addFab", useUnmergedTree = true).performClick()

        // Assert, has entered Create Supplier Dialog
        composeTestRule.onNodeWithText("Create Supplier").assertIsDisplayed()

        // Interact with Company Name field via placeholder, and enter new input
        composeTestRule.onNodeWithText("Enter company name")
            .performTextInput("New Company")

        // --- Interact with Item Name Selector ---
        // 1. Click the selector via placeholder text to open selection sheet
        composeTestRule.onNodeWithText("Select item name").performClick()

        // 2. Check for "Item A" that is of Radio Button to select
        // -> Radio Button is used to indicate the ability to choose only one selection
        // -> The added specification is to remove ambiguity by differencing the "Item A" in Supplier Main Menu and the Selector
        composeTestRule.onNode(hasText("Item A") and hasRole(Role.RadioButton)).performClick()

        // 3. Find and click the "Apply" button to confirm selection
        composeTestRule.onNodeWithText("Apply").performClick()

        // --- Interact with SKU Selector ---
        // 1. Click the selector via placeholder text to open selection sheet
        composeTestRule.onNodeWithText("Select SKU").performClick()

        // 2. Check for "Sku A" that is of Checkbox Button to select
        // -> Checkbox Button is used to indicate the ability to choose multiple selection
        // -> The added specification is to remove ambiguity by differencing the "Sku A" in Supplier Main Menu and the Selector
        composeTestRule.onNode(hasText("Sku A") and hasRole(Role.Checkbox)).performClick()

        // 3. Find and click the "Apply" button to confirm selection
        composeTestRule.onNodeWithText("Apply").performClick()

        // Find a button with "Supplied Item" text
        // This button is responsible to add another item selector to the list
        composeTestRule.onNodeWithText("Supplied Item").performClick()

        // Repeat the same step -> Item Name
        composeTestRule.onNodeWithText("Select item name").performClick()
        composeTestRule.onNode(hasText("Item B") and hasRole(Role.RadioButton)).performClick()
        composeTestRule.onNodeWithText("Apply").performClick()

        // Repeat the same step -> Sku (but we choose 2 Sku here)
        composeTestRule.onNodeWithText("Select SKU").performClick()
        composeTestRule.onNode(hasText("Sku A") and hasRole(Role.Checkbox)).performClick()
        composeTestRule.onNode(hasText("Sku B") and hasRole(Role.Checkbox)).performClick()
        composeTestRule.onNodeWithText("Apply").performClick()

        // Find Create Button and Submit Form and let Compose Test Rule handle synchronization
        composeTestRule.onNodeWithText("Create").performClick()

        // Wait until the new supplier's name appears on the home screen.
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            try {
                composeTestRule.onNodeWithText("New Company").assertIsDisplayed()
                composeTestRule.onNodeWithText("Success, supplier has been added.")
                    .assertIsDisplayed()
                true
            } catch (_: AssertionError) {
                false
            }
        }


        // Verify the underlying fake data source was also updated correctly.
        assertEquals(
            3, fakeApiDataSource.getSuppliersData().size,
            "The fake data source should now contain three suppliers."
        )

        Log.e("UI TESTING: New Supplier Created ID", fakeApiDataSource.getSuppliersData()[2].id)
    }

    @Test
    fun deleteSupplier_removesItemFromList() {
        // --- ARRANGE ---
        val initialSupplier = INITIAL_SUPPLIER_DTO
        val supplierToDelete = initialSupplier[0] // This is "Company A" with id "1"
        val supplierToKeep = initialSupplier[1] // This is "Company B" with id "2"

        // 1. Verify Initial State: Both suppliers are present
        composeTestRule.onNodeWithText(supplierToDelete.companyName).assertIsDisplayed()
        composeTestRule.onNodeWithText(supplierToKeep.companyName).assertIsDisplayed()
        assertEquals(
            2,
            fakeApiDataSource.getSuppliersData().size,
            "PRE-CONDITION: Fake data source should have 2 suppliers."
        )

        Log.e(
            "UI TESTING: Supplier to Delete ID",
            fakeApiDataSource.getSuppliersData()[supplierToDelete.id.toInt()].id
        )

        // --- ACT ---
        // 2. Find all "More" icon button via contentDescription, but because it scans multiple items, we specify which one to pick by using the index of the supplier we want to delete
        // -> and then Click it
        composeTestRule.onAllNodes(hasContentDescription("More"))[initialSupplier.indexOf(
            supplierToDelete
        )].performClick()


        // 3. Action Sheet appears, find Delete button and perform Click
        composeTestRule.onNodeWithText("Delete").performClick()

        // 4. Confirmation Dialog with "Delete Supplier" Title appeared
        composeTestRule.onNodeWithText("Delete Supplier").assertIsDisplayed()
        // There is currently two Delete button visible, the current Delete button in the confirmation dialog, and the former in the Action Sheet
        // So we need to specify which button we want to use by using whatever is most plausible connection. In this case, the Delete Button is in the "Delete Supplier" confirmation dialog. So we use the title sibling as the specification
        composeTestRule.onNode(hasText("Delete") and hasAnySibling(hasText("Delete Supplier")))
            .performClick()


        // --- ASSERT ---
        // 5. Verify the item is no longer on the screen.
        composeTestRule.onNodeWithText(supplierToDelete.companyName).assertDoesNotExist()
        composeTestRule.onNodeWithText("Success, supplier has been deleted.").assertIsDisplayed()

        // 6. Verify the other item is still visible.
        composeTestRule.onNodeWithText(supplierToKeep.companyName).assertIsDisplayed()

        // 7. Verify the underlying fake data source was updated correctly.
        assertEquals(
            1, fakeApiDataSource.getSuppliersData().size,
            "The fake data source should now contain only one supplier."
        )
        assertEquals(supplierToKeep.id, fakeApiDataSource.getSuppliersData().first().id)
    }

    @Test
    fun editSupplier_changeItemName() {
        // ASSERT
        val initialSupplier = INITIAL_SUPPLIER_DTO
        val supplierToEdit = initialSupplier.first()
        val initialName = supplierToEdit.companyName // "Company A"
        val editedName = "Edited Name"
        assertEquals(initialName, supplierToEdit.companyName, "Initial name is 'Company A'")

        // Assert that the item with the Company A name exist and displayed
        composeTestRule.onNodeWithText(initialName).assertIsDisplayed()

        // Find all Show More Icon via "More" content Description and specify the item to be edited using supplierToEdit index
        composeTestRule.onAllNodes(hasContentDescription("More"))[initialSupplier.indexOf(
            supplierToEdit
        )].performClick()

        // Click Edit in Action Sheet
        composeTestRule.onNodeWithText("Edit").performClick()

        // Assert, has entered the Edit Supplier Screen by using Title
        composeTestRule.onNodeWithText("Edit Supplier").assertIsDisplayed()

        // Find the Company Name field by using the already existing name, and isEditable (because it might contradict with the Company Name in the Supplier Screen)
        // -> and then perform change name
        composeTestRule.onNode(hasText(supplierToEdit.companyName) and isEditable())
            .performTextReplacement(editedName)
        // Find the "Edit" Button and Submit the changed form
        composeTestRule.onNode(hasText("Edit")).performClick()

        composeTestRule.onNodeWithText(editedName).assertIsDisplayed()
        composeTestRule.onNodeWithText("Success, supplier has been edited.")
            .assertIsDisplayed()

        // Wait until the edit dialog close and the edited name is displayed
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            try {
                composeTestRule.onNodeWithText(editedName).assertIsDisplayed()
                composeTestRule.onNodeWithText("Success, supplier has been edited.")
                    .assertIsDisplayed()
                true
            } catch (_: AssertionError) {
                false
            }
        }

        // Assert that the old initial name doesn't exist anymore
        composeTestRule.onNodeWithText(initialName).assertDoesNotExist()

        // Assert that the edited name is truly exist in the data
        val editedSupplierInData =
            fakeApiDataSource.getSuppliersData().firstOrNull { it.id == supplierToEdit.id }
        assertEquals(editedName, editedSupplierInData?.companyName)
    }
}

private fun hasRole(role: Role) = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)