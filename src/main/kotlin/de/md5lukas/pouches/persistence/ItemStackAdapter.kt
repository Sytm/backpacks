package de.md5lukas.pouches.persistence

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

object ItemStackAdapter : PersistentDataType<ByteArray, ItemStack> {
  override fun getPrimitiveType(): Class<ByteArray> = ByteArray::class.java

  override fun getComplexType(): Class<ItemStack> = ItemStack::class.java

  override fun fromPrimitive(
      primitive: ByteArray,
      context: PersistentDataAdapterContext
  ): ItemStack =
      if (primitive.isEmpty()) {
        ItemStack(Material.AIR)
      } else {
        ItemStack.deserializeBytes(primitive)
      }

  override fun toPrimitive(complex: ItemStack, context: PersistentDataAdapterContext): ByteArray =
      if (complex.type.isAir) {
        ByteArray(0)
      } else {
        complex.serializeAsBytes()
      }
}
