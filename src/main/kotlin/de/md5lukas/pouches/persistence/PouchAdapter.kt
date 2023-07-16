package de.md5lukas.pouches.persistence

import de.md5lukas.pouches.PouchesPlugin
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class PouchAdapter(
    private val plugin: PouchesPlugin,
) : PersistentDataType<PersistentDataContainer, Pouch> {

  private val size = NamespacedKey.fromString("size", plugin)!!
  private val content = NamespacedKey.fromString("content", plugin)!!
  private val itemStacksAdapter = ItemStacksAdapter(plugin)

  override fun getPrimitiveType(): Class<PersistentDataContainer> =
      PersistentDataContainer::class.java

  override fun getComplexType(): Class<Pouch> = Pouch::class.java

  override fun fromPrimitive(
      primitive: PersistentDataContainer,
      context: PersistentDataAdapterContext
  ): Pouch =
      Pouch(
          plugin = plugin,
          size = primitive.getOrDefault(size, PersistentDataType.INTEGER, 1),
          contents = primitive.get(content, itemStacksAdapter) ?: emptyArray())

  override fun toPrimitive(
      complex: Pouch,
      context: PersistentDataAdapterContext
  ): PersistentDataContainer {
    val container = context.newPersistentDataContainer()

    container.set(size, PersistentDataType.INTEGER, complex.size)
    container.set(content, itemStacksAdapter, complex.contents)

    return container
  }
}
