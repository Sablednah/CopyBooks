/*******************************************************************************
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 ******************************************************************************/

/*
 * Copyright (C) 2012 p000ison
 * 
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 * 
 */
package com.p000ison.dev.copybooks.commands;

import com.p000ison.dev.copybooks.CopyBooks;
import com.p000ison.dev.copybooks.api.CraftWrittenBook;
import com.p000ison.dev.copybooks.api.InvalidBookException;
import com.p000ison.dev.copybooks.objects.GenericCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Max
 */
public class UnsignBookCommand extends GenericCommand {

    public UnsignBookCommand(CopyBooks plugin, String name)
    {
        super(plugin, name);
        setPermissions("cb.command.unsign");
        setUsages("/cb unsign §f- Unsings a book");
        setArgumentRange(0, 0);
        setIdentifiers("unsign", "us");
        setPermissions("cb.admin.unsign");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack item = player.getItemInHand();

            if (item == null || !item.getType().equals(Material.WRITTEN_BOOK)) {
                player.sendMessage(ChatColor.RED + plugin.getTranslation("book.not.written"));
                return;
            }

            try {
                CraftWrittenBook book = new CraftWrittenBook(item);

                if (!book.unsign()) {
                    player.sendMessage(ChatColor.RED + plugin.getTranslation("book.not.signed"));
                    return;
                }

                ItemStack itemStack = book.toItemStack(item.getAmount());
                itemStack.setTypeId(386);

                player.setItemInHand(itemStack);
                player.updateInventory();
            } catch (InvalidBookException e) {
                CopyBooks.debug(null, e);
                return;
            }

            sender.sendMessage(ChatColor.GREEN + plugin.getTranslation("book.unsigned"));
        } else {
            sender.sendMessage(ChatColor.RED + plugin.getTranslation("only.players"));
        }
    }
}
