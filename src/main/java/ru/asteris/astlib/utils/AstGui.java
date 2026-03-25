package ru.asteris.astlib.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AstGui implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> actions;
    private Consumer<InventoryOpenEvent> openAction;
    private Consumer<InventoryCloseEvent> closeAction;
    private boolean cancelClicks;
    private final int size;

    public AstGui(int size, String title) {
        this.size = size;
        this.inventory = Bukkit.createInventory(this, size, ColorUtils.parse(title));
        this.actions = new HashMap<>();
        this.cancelClicks = true;
    }

    public AstGui setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    public AstGui setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> action) {
        inventory.setItem(slot, item);
        if (action != null) {
            actions.put(slot, action);
        }
        return this;
    }

    public AstGui setItem(int row, int col, ItemStack item) {
        return setItem(row * 9 + col, item);
    }

    public AstGui setItem(int row, int col, ItemStack item, Consumer<InventoryClickEvent> action) {
        return setItem(row * 9 + col, item, action);
    }

    public AstGui fill(ItemStack item) {
        for (int i = 0; i < size; i++) {
            if (inventory.getItem(i) == null) {
                setItem(i, item);
            }
        }
        return this;
    }

    public AstGui fillBorder(ItemStack item) {
        int rows = size / 9;
        for (int i = 0; i < size; i++) {
            int row = i / 9;
            int col = i % 9;
            if (row == 0 || row == rows - 1 || col == 0 || col == 8) {
                if (inventory.getItem(i) == null) {
                    setItem(i, item);
                }
            }
        }
        return this;
    }

    public AstGui fillRow(int row, ItemStack item) {
        for (int col = 0; col < 9; col++) {
            if (inventory.getItem(row * 9 + col) == null) {
                setItem(row * 9 + col, item);
            }
        }
        return this;
    }

    public AstGui fillColumn(int col, ItemStack item) {
        int rows = size / 9;
        for (int row = 0; row < rows; row++) {
            if (inventory.getItem(row * 9 + col) == null) {
                setItem(row * 9 + col, item);
            }
        }
        return this;
    }

    public AstGui clear(int slot) {
        inventory.setItem(slot, null);
        actions.remove(slot);
        return this;
    }

    public AstGui clear() {
        inventory.clear();
        actions.clear();
        return this;
    }

    public AstGui setCancelClicks(boolean cancelClicks) {
        this.cancelClicks = cancelClicks;
        return this;
    }

    public boolean isCancelClicks() {
        return cancelClicks;
    }

    public AstGui onOpen(Consumer<InventoryOpenEvent> action) {
        this.openAction = action;
        return this;
    }

    public AstGui onClose(Consumer<InventoryCloseEvent> action) {
        this.closeAction = action;
        return this;
    }

    public void handleOpen(InventoryOpenEvent event) {
        if (openAction != null) {
            openAction.accept(event);
        }
    }

    public void handleClose(InventoryCloseEvent event) {
        if (closeAction != null) {
            closeAction.accept(event);
        }
    }

    public void handleClick(InventoryClickEvent event) {
        Consumer<InventoryClickEvent> action = actions.get(event.getRawSlot());
        if (action != null) {
            action.accept(event);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}