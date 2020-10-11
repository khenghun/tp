package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertInventoryCommandFailure;
import static seedu.address.testutil.TypicalItems.getTypicalItemList;
import static seedu.address.testutil.TypicalLocations.getTypicalLocationsList;
import static seedu.address.testutil.TypicalRecipes.APPLE_PIE;
import static seedu.address.testutil.TypicalRecipes.getTypicalRecipeList;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class DeleteRecipeCommandIntegrationTest {

    private final Model model = new ModelManager(getTypicalItemList(), getTypicalLocationsList(),
            getTypicalRecipeList(), new UserPrefs());

    /**
     * Tests for successful deletion of a recipe found in the recipe list.
     */
    @Test
    public void execute_success() {
        DeleteRecipeCommand drc = new DeleteRecipeCommand("Apple", Index.fromOneBased(1));
        Model expectedModel = new ModelManager(getTypicalItemList(), getTypicalLocationsList(),
                getTypicalRecipeList(), new UserPrefs());
        expectedModel.deleteRecipe(APPLE_PIE);
        String expectedMessage = String.format(DeleteRecipeCommand.MESSAGE_SUCCESS, APPLE_PIE);
        assertCommandSuccess(drc, model, expectedMessage, expectedModel);
    }

    /**
     * Tests for failure of deletion of recipe when the product name cannot be found in the recipe list.
     */
    @Test
    public void execute_recipeNotFound() {
        DeleteRecipeCommand drc = new DeleteRecipeCommand("Cake", Index.fromOneBased(1));
        // Cake does not exist in recipe list
        assertInventoryCommandFailure(drc, model, DeleteRecipeCommand.MESSAGE_RECIPE_NOT_FOUND);
    }

    /**
     * Tests for failure of deletion of recipe when product name can be found but index is out of range.
     */
    @Test
    public void execute_indexOutOfRange() throws CommandException {
        DeleteRecipeCommand drc = new DeleteRecipeCommand("Apple", Index.fromOneBased(2));
        // index out of range since only 1 recipe for apple
        drc.execute(model);
        assertInventoryCommandFailure(drc, model, DeleteRecipeCommand.MESSAGE_INDEX_NOT_FOUND);
    }
}