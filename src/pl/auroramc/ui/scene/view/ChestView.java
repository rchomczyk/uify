package pl.auroramc.ui.scene.view;

public record ChestView(int height, int width) implements SceneView {

    private static final int COLUMNS_PER_ROW = 9;

    public static ChestView ofRows(final int rows) {
        return new ChestView(rows, COLUMNS_PER_ROW);
    }

    @Override
    public int columnsPerRow() {
        return COLUMNS_PER_ROW;
    }

    @Override
    public int estimatedSize() {
        return height * width;
    }
}
