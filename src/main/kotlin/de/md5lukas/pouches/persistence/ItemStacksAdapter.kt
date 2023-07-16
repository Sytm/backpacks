package de.md5lukas.pouches.persistence

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

class ItemStacksAdapter(
    plugin: Plugin,
) : PersistentDataType<Array<PersistentDataContainer>, Array<ItemStack?>> {

  private val air = ItemStack(Material.AIR)
  private val content = NamespacedKey.fromString("content", plugin)!!

  override fun getPrimitiveType(): Class<Array<PersistentDataContainer>> =
      Array<PersistentDataContainer>::class.java

  override fun getComplexType(): Class<Array<ItemStack?>> = Array<ItemStack?>::class.java

  override fun fromPrimitive(
      primitive: Array<PersistentDataContainer>,
      context: PersistentDataAdapterContext,
  ): Array<ItemStack?> =
      primitive.map { it.getOrDefault(content, ItemStackAdapter, air) }.toTypedArray()

  override fun toPrimitive(
      complex: Array<ItemStack?>,
      context: PersistentDataAdapterContext,
  ): Array<PersistentDataContainer> {
    return complex
        .map { stack ->
          val container = context.newPersistentDataContainer()
          if (stack != null) {
            container.set(content, ItemStackAdapter, stack)
          }
          container
        }
        .toTypedArray()
  }
}
