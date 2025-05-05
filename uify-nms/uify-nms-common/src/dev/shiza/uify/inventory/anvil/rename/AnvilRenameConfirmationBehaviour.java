package dev.shiza.uify.inventory.anvil.rename;

@FunctionalInterface
public interface AnvilRenameConfirmationBehaviour {

    void accept(final AnvilRenameConfirmationBehaviourState state);

    default AnvilRenameConfirmationBehaviour andThen(final AnvilRenameConfirmationBehaviour after) {
        if (after == null) {
            throw new NullPointerException("The 'after' CanvasElementBehaviour cannot be null");
        }

        return state -> {
            this.accept(state);
            after.accept(state);
        };
    }
}
