package dev.shiza.uify.inventory.anvil;

import io.papermc.paper.adventure.AdventureComponent;
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

    private static final VarHandle BUKKIT_OWNER_HANDLE;
    private static final int INPUT_SLOTS = 2;

    static {
        try {
            final MethodHandles.Lookup lookupSimpleContainer =
                MethodHandles.privateLookupIn(SimpleContainer.class, MethodHandles.lookup());
            BUKKIT_OWNER_HANDLE =
                lookupSimpleContainer.findVarHandle(SimpleContainer.class, "bukkitOwner", InventoryHolder.class);
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            throw new AnvilInventoryProducingException(
                "Could not initialize var handle for bukkit owner of anvil inventory", exception);
        }
    }

    private final ServerPlayer player;
    private final CraftAnvilView view;
    private String renameText;
    private boolean visible;

    AnvilInventoryV1_21_4(final Player player, final Component title) {
        this(((CraftPlayer) player).getHandle(), title);
    }

    AnvilInventoryV1_21_4(
        final ServerPlayer player, final Component title) {
        super(
            player.nextContainerCounter(),
            player.getInventory(),
            ContainerLevelAccess.create(player.level(), new BlockPos(0, 0, 0)));
        this.checkReachable = false;

        setTitle(new AdventureComponent(title));

        this.player = player;

        final CraftInventoryAnvil inventory =
            new CraftInventoryAnvil(access.getLocation(), inputSlots, resultSlots);
        this.view = new CraftAnvilView(player.getBukkitEntity(), inventory, this);
    }

    @Override
    public void open() {
        visible = true;

        CraftEventFactory.callInventoryOpenEvent(player, this);

        final NonNullList<ItemStack> items = NonNullList.of(ItemStack.EMPTY, item(0), item(1), item(2));
        player.containerMenu = this;
        player.connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.ANVIL, getTitle()));
        player.connection.send(
            new ClientboundContainerSetContentPacket(
                player.containerMenu.containerId,
                incrementStateId(),
                items,
                ItemStack.EMPTY));
        player.initMenu(this);
    }

    @Override
    public void holder(final InventoryHolder holder) {
        BUKKIT_OWNER_HANDLE.set(inputSlots, holder);
    }

    @Override
    public void renameText(final String renameText) {
        this.renameText = renameText;

        cost.set(0);

        ItemStack result = inputSlots.getItem(0);
        if (result.isEmpty()) {
            return;
        }

        result = result.copy();
        result.set(DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal(renameText));

        inputSlots.setItem(0, result);

        if (visible) {
            sendAllDataToRemote();
            broadcastChanges();
        }
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
    public void removed(final net.minecraft.world.entity.player.Player player) {
        visible = false;
    }

    @Override
    protected void clearContainer(final net.minecraft.world.entity.player.Player player, final Container container) {
        visible = false;
    }

    @Override
    public void createResult() {
    }

    @Override
    public boolean stillValid(final net.minecraft.world.entity.player.Player player) {
        return true;
    }

    @Override
    public boolean setItemName(final String itemName) {
        renameText = itemName;

        super.setItemName(itemName);
        return false;
    }

    private ItemStack item(final int slot) {
        return slot < INPUT_SLOTS ? inputSlots.getItem(slot) : resultSlots.getItem(0);
    }
}
