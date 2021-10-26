package dev.emortal.lobby.blockhandler

import net.kyori.adventure.key.Key
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID
import org.jglrxavpok.hephaistos.nbt.NBTCompound

object SkullHandler : BlockHandler {
    override fun getNamespaceId(): NamespaceID = NamespaceID.from(Key.key("minecraft:skull"))
    override fun getBlockEntityTags(): MutableCollection<Tag<*>> {
        val tags = mutableListOf<Tag<*>>()

        tags.add(Tag.String("ExtraType"))

        val compound: Tag<NBTCompound> = Tag.NBT("SkullOwner");
        tags.add(compound)

        return tags
    }
}