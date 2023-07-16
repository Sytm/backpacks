package de.md5lukas.pouches

import de.md5lukas.pouches.persistence.Pouch
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent

class Events(
    private val plugin: PouchesPlugin,
) : Listener {

  @EventHandler
  fun on(e: PlayerInteractEvent) {
    if (e.action.isRightClick) {
      e.item?.let { eventItem ->
        val container = eventItem.itemMeta.persistentDataContainer
        if (container.has(plugin.pouchKey)) {
          val pouch = container.get(plugin.pouchKey, plugin.pouchAdapter)!!
          pouch.stack = eventItem
          pouch.open(e.player)
          e.isCancelled = true
        }
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  fun on(e: InventoryClickEvent) {
    val currentItem = e.currentItem ?: return
    val dataContainer = currentItem.itemMeta?.persistentDataContainer ?: return

    if (dataContainer.has(plugin.pouchKey)) {
      if (e.view.topInventory.holder is Pouch) {
        e.isCancelled = true
        return
      }

      val cursor = e.cursor ?: return

      if (cursor.type === Material.LEATHER && cursor.amount >= 9) {
        e.isCancelled = true

        val pouch = dataContainer.get(plugin.pouchKey, plugin.pouchAdapter)!!
        if (pouch.size < 6) {
          pouch.size += 1

          currentItem.editMeta {
            it.displayName(plugin.itemNames[pouch.size - 1])
            it.persistentDataContainer.set(plugin.pouchKey, plugin.pouchAdapter, pouch)
          }
          e.currentItem = currentItem

          cursor.amount -= 9

          e.isCancelled = true

          e.whoClicked.scheduler.run(plugin, { e.view.cursor = cursor }, null)
        }
      }
    }
  }

  @EventHandler
  fun on(e: InventoryCloseEvent) {
    val holder = e.view.topInventory.holder
    if (holder is Pouch) {
      holder.save()
    }
  }
}
