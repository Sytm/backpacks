package de.md5lukas.pouches.persistence

import de.md5lukas.pouches.PouchesPlugin
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class Pouch(private val plugin: PouchesPlugin, var size: Int, var contents: Array<ItemStack?>) :
    InventoryHolder {

  lateinit var stack: ItemStack
  private lateinit var inventory: Inventory

  override fun getInventory(): Inventory {
    if (!::inventory.isInitialized) {
      inventory = plugin.server.createInventory(this, size * 9, plugin.itemNames[size - 1])
      inventory.contents = contents
    }
    return inventory
  }

  fun open(player: Player) {
    player.openInventory(getInventory())
  }

  fun save() {
    if (::inventory.isInitialized) {
      this.contents = inventory.contents
    }
    stack.editMeta { it.persistentDataContainer.set(plugin.pouchKey, plugin.pouchAdapter, this) }
  }
}
