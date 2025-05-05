package dev.shiza.uify.inventory.anvil;

import io.papermc.paper.adventure.PaperAdventure;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftInventoryAnvil;
import org.bukkit.craftbukkit.inventory.view.CraftAnvilView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

final class AnvilInventoryV1_21_4 extends AnvilMenu implements AnvilInventory {

    private static final VarHandle BUKKIT_INPUT_SLOTS_OWNER_HANDLE;
    private static final VarHandle BUKKIT_RESULT_SLOTS_OWNER_HANDLE;
    private static final int INPUT_SLOTS = 2;

    static {
        try {
            final MethodHandles.Lookup lookupSimpleContainer =
                MethodHandles.privateLookupIn(SimpleContainer.class, MethodHandles.lookup());
            BUKKIT_INPUT_SLOTS_OWNER_HANDLE =
                lookupSimpleContainer.findVarHandle(SimpleContainer.class, "bukkitOwner", InventoryHolder.class);
            final MethodHandles.Lookup lookupResultContainer =
                MethodHandles.privateLookupIn(ResultContainer.class, MethodHandles.lookup());
            BUKKIT_RESULT_SLOTS_OWNER_HANDLE =
                lookupResultContainer.findVarHandle(ResultContainer.class, "holder", InventoryHolder.class);
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            throw new AnvilInventoryProducingException(
                "Could not initialize var handle for bukkit owner of anvil inventory", exception);
        }
    }

    private final ServerPlayer viewer;
    private final CraftAnvilView view;
    private String renameText;
    private boolean visible;

    AnvilInventoryV1_21_4(final Player viewer, final Component title) {
        this(((CraftPlayer) viewer).getHandle(), title);
    }

    AnvilInventoryV1_21_4(
        final ServerPlayer viewer, final Component title) {
        super(
            viewer.nextContainerCounter(),
            viewer.getInventory(),
            ContainerLevelAccess.create(viewer.level(), new BlockPos(0, 0, 0)));
        this.checkReachable = false;

        setTitle(PaperAdventure.asVanilla(title));

        this.viewer = viewer;

        final CraftInventoryAnvil inventory =
            new CraftInventoryAnvil(access.getLocation(), inputSlots, resultSlots);
        this.view = new CraftAnvilView(viewer.getBukkitEntity(), inventory, this);
    }

    @Override
    public void open() {
        visible = true;

        CraftEventFactory.callInventoryOpenEvent(viewer, this);

        final NonNullList<ItemStack> items = NonNullList.of(ItemStack.EMPTY, item(0), item(1), item(2));
        viewer.containerMenu = this;
        viewer.connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.ANVIL, getTitle()));
        viewer.connection.send(
            new ClientboundContainerSetContentPacket(
                viewer.containerMenu.containerId,
                incrementStateId(),
                items,
                ItemStack.EMPTY));
        viewer.initMenu(this);
    }

    @Override
    public void holder(final InventoryHolder holder) {
        BUKKIT_INPUT_SLOTS_OWNER_HANDLE.set(inputSlots, holder);
        BUKKIT_RESULT_SLOTS_OWNER_HANDLE.set(resultSlots, holder);
    }

    @Override
    public String renameText() {
        return renameText;
    }

    @Override
    public @NotNull CraftAnvilView getBukkitView() {
        return view;
    }

    @Override
    public @NotNull Inventory getBukkitInventory() {
        return view.getTopInventory();
    }

    @Override
    public void removed(final net.minecraft.world.entity.player.@NotNull Player player) {
        visible = false;
    }

    @Override
    protected void clearContainer(
        final net.minecraft.world.entity.player.@NotNull Player player,
        final @NotNull Container container) {
        visible = false;
    }

    @Override
    public void createResult() {
        final ItemStack copy = inputSlots.getItem(0).copy();
        if (renameText == null || renameText.isEmpty()) {
            copy.remove(DataComponents.CUSTOM_NAME);
        } else {
            copy.set(DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal(renameText));
        }
        resultSlots.setItem(0, copy);

        if (visible) {
            sendAllDataToRemote();
            broadcastChanges();
        }
    }

    @Override
    public boolean stillValid(final net.minecraft.world.entity.player.@NotNull Player player) {
        return true;
    }

    @Override
    public boolean setItemName(final @NotNull String itemName) {
        renameText = itemName;

        createResult();
        return false;
    }

    @Override
    public void renameText(final String renameText) {
        setItemName(renameText);
    }

    @Override
    protected boolean mayPickup(final net.minecraft.world.entity.player.@NotNull Player player, final boolean hasStack) {
        return false;
    }

    private ItemStack item(final int slot) {
        return slot < INPUT_SLOTS ? inputSlots.getItem(slot) : resultSlots.getItem(0);
    }
}
