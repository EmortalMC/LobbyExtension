package dev.emortal.lobby.inventories

import dev.emortal.immortal.game.GameManager
import dev.emortal.immortal.game.GameManager.joinGameOrNew
import dev.emortal.lobby.LobbyExtension
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemHideFlag
import net.minestom.server.item.ItemStack
import world.cepi.kstom.adventure.asMini
import world.cepi.kstom.adventure.noItalic
import world.cepi.kstom.item.item
import world.cepi.kstom.util.setItemStacks

object GameSelectorInventory {
    val inventory: Inventory

    init {
        val inventoryTitle = Component.text("Games", NamedTextColor.BLACK)
        inventory = Inventory(InventoryType.CHEST_6_ROW, inventoryTitle)

        val itemStackMap = mutableMapOf<Int, ItemStack>()

        LobbyExtension.gameListingConfig.gameListings.forEach {
            if (!it.value.visible) return@forEach

            val gameClass = GameManager.gameNameToClassMap[it.key] ?: return@forEach
            val gameType = GameManager.registeredGameMap[gameClass] ?: return@forEach

            val loreList = it.value.description.toMutableList()
            loreList.addAll(listOf(
                "",
                "<dark_gray>/play ${it.key}",
                "<green>● <bold>${GameManager.gameMap[it.key]?.size ?: 0}</bold> playing"
            ))

            itemStackMap[it.value.slot] = item(it.value.item) {
                displayName(gameType.gameTitle.noItalic())
                lore(loreList.map { loreLine -> loreLine.asMini().noItalic() })
                hideFlag(*ItemHideFlag.values())
                setTag(GameManager.gameNameTag, it.key)
            }
        }

        inventory.setItemStacks(itemStackMap)

        inventory.addInventoryCondition { player, _, _, inventoryConditionResult ->
            inventoryConditionResult.isCancel = true

            if (inventoryConditionResult.clickedItem == ItemStack.AIR) return@addInventoryCondition

            if (inventoryConditionResult.clickedItem.hasTag(GameManager.gameNameTag)) {
                val gameName = inventoryConditionResult.clickedItem.getTag(GameManager.gameNameTag) ?: return@addInventoryCondition
                player.joinGameOrNew(gameName)
            }
        }
    }

}