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
import com.p000ison.dev.copybooks.GenericCommand;
import net.minecraft.server.Item;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * @author Max
 */
public class CreateBookCommand extends GenericCommand {

    public CreateBookCommand(CopyBooks plugin, String name)
    {
        super(plugin, name);
        setPermissions("cb.command.create");
        setUsages("/cb create - Creates a book from a id");
        setArgumentRange(1, 2);
        setIdentifiers("create", "c");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack item = player.getItemInHand();

            if (item == null) {
                return;
            }

            int amount = 1;

            if (args.length == 2) {
                amount = Integer.parseInt(args[1]);
            }

            int id = Integer.parseInt(args[0]);
            ItemStack book = plugin.getBookManager().getBookById(id, amount);

            if (book == null) {
                sender.sendMessage("Book not found!");
                return;
            }
            player.getInventory().addItem(plugin.getBookManager().getBookById(id, amount));
            player.updateInventory();

            sender.sendMessage(String.format("You got %s book with the id %s", amount, id));
        } else {
            sender.sendMessage(plugin.getTranslation("only.player"));
        }
    }
}
