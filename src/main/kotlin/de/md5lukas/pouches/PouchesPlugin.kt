package de.md5lukas.pouches

import de.md5lukas.pouches.persistence.PouchAdapter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class PouchesPlugin : JavaPlugin() {

  val pouchAdapter = PouchAdapter(this)
  val pouchKey = NamespacedKey.fromString("pouch", this)!!

  val itemNames =
      listOf<Component>(
          Component.text { builder ->
            builder.decoration(TextDecoration.ITALIC, false)
            builder.color(NamedTextColor.GRAY)
            builder.content("Improvised Pouch")
          },
          Component.text { builder ->
            builder.decoration(TextDecoration.ITALIC, false)
            builder.color(NamedTextColor.BLUE)
            builder.content("Scuffed Pouch")
          },
          Component.text { builder ->
            builder.decoration(TextDecoration.ITALIC, false)
            builder.color(NamedTextColor.GREEN)
            builder.content("Basic Pouch")
          },
          Component.text { builder ->
            builder.decoration(TextDecoration.ITALIC, false)
            builder.color(NamedTextColor.YELLOW)
            builder.content("Padded Pouch")
          },
          Component.text { builder ->
            builder.decoration(TextDecoration.ITALIC, false)
            builder.color(NamedTextColor.AQUA)
            builder.content("Modified Pouch")
          },
          Component.text { builder ->
            builder.decoration(TextDecoration.ITALIC, false)
            builder.color(NamedTextColor.DARK_PURPLE)
            builder.content("Enlarged Pouch")
          },
      )

  override fun onEnable() {
    server.pluginManager.registerEvents(Events(this), this)

    registerRecipes()
  }

  private fun registerRecipes() {
    val basicItem = ItemStack(Material.LEATHER)

    basicItem.editMeta {
      it.displayName(itemNames.first())

      it.persistentDataContainer.set(
          pouchKey,
          PersistentDataType.TAG_CONTAINER,
          it.persistentDataContainer.adapterContext.newPersistentDataContainer(),
      )
    }

    val basicRecipe = ShapedRecipe(NamespacedKey.fromString("pouch1", this)!!, basicItem)
    basicRecipe.shape(
        "lll",
        "l l",
        "lll",
    )
    basicRecipe.setIngredient('l', Material.LEATHER)
    basicRecipe.group = "pouches"
    basicRecipe.category = CraftingBookCategory.EQUIPMENT

    server.addRecipe(basicRecipe)
  }
}
