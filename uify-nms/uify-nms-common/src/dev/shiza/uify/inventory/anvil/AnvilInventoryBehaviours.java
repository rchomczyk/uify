package dev.shiza.uify.inventory.anvil;

import dev.shiza.uify.inventory.anvil.rename.AnvilRenameConfirmationBehaviour;

public interface AnvilInventoryBehaviours {

    AnvilInventory onRenameConfirmation(final AnvilRenameConfirmationBehaviour behaviour);
}
