package com.p000ison.dev.copybooks.util;

import com.p000ison.dev.copybooks.CopyBooks;
import com.p000ison.dev.copybooks.api.CraftWrittenBook;
import com.p000ison.dev.copybooks.api.InvalidBookException;
import com.p000ison.dev.copybooks.api.WrittenBook;
import org.spout.nbt.CompoundTag;
import org.spout.nbt.ListTag;
import org.spout.nbt.StringTag;
import org.spout.nbt.Tag;
import org.spout.nbt.stream.NBTInputStream;
import org.spout.nbt.stream.NBTOutputStream;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a BookIO
 */
public final class BookIO {


    public static void writeBook(List<String> book, Writer writer) throws IOException
    {

        for (String page : book) {
            writer.write(page + "\n");
        }

        writer.flush();
        writer.close();
    }

    public static boolean writeNBTBook(WrittenBook book, FileOutputStream fileOutputStream) throws IOException
    {
        NBTOutputStream outputStream;

        try {
            outputStream = new NBTOutputStream(fileOutputStream);

            List<Tag> contents = new ArrayList<Tag>();

            contents.add(new StringTag("title", book.getTitle()));
            contents.add(new StringTag("author", book.getAuthor()));

            List<StringTag> tagPages = new ArrayList<StringTag>();

            for (String page : book.getPages()) {
                tagPages.add(new StringTag("page", page));
            }

            ListTag<StringTag> pages = new ListTag<StringTag>("pages", StringTag.class, tagPages);

            contents.add(pages);

            CompoundTag root = new CompoundTag("Book", contents);

            outputStream.writeTag(root);

            outputStream.close();

            return true;
        } catch (IOException e) {
            CopyBooks.debug("Failed at writing book: " + book.getTitle(), e);
            return false;
        }
    }

    public static WrittenBook readNBTBook(FileInputStream fileInputStream) throws IOException
    {
        NBTInputStream inputStream;
        try {
            inputStream = new NBTInputStream(fileInputStream);

            CraftWrittenBook book = new CraftWrittenBook();


            CompoundTag root = (CompoundTag) inputStream.readTag();

            if (root == null) {
                return null;
            }

            List<Tag> contents = root.getValue();

            if (contents == null) {
                return null;
            }

            StringTag title = (StringTag) contents.get(0);
            StringTag author = (StringTag) contents.get(1);
            ListTag<StringTag> pages = (ListTag<StringTag>) contents.get(2);

            List<String> convertedPages = new ArrayList<String>();

            for (StringTag page : pages.getValue()) {
                convertedPages.add(page.getValue());
            }


            book.setTitle(title.getValue());
            book.setAuthor(author.getValue());
            book.setPages(convertedPages);

            inputStream.close();

            return book;
        } catch (IOException e) {
            CopyBooks.debug("Failed at reading book", e);
            return null;
        } catch (Exception e) {
            CopyBooks.debug("Failed at reading book", e);
            return null;
        }
    }

    public static List<String> readBook(BufferedReader reader) throws IOException, InvalidBookException
    {
        List<String> list = new ArrayList<String>();

        String page;

        while ((page = reader.readLine()) != null) {
            list.add(page);
        }


        reader.close();
        return list;
    }

    /**
     * Reads pages from a stream unformated  and adds them to a list
     *
     * @param reader The input stream to reate the pages from
     * @return The created pages from this stream
     * @throws IOException
     */
    public static List<String> readUnformattedBook(Reader reader) throws IOException
    {
        List<String> pages = new ArrayList<String>();
        char[] buffer = new char[256];

        int iterations = 0;

        while (reader.read(buffer) != -1) {
            iterations++;

            if (iterations >= 50) {
                break;
            }

            pages.add(new String(buffer));
        }

        reader.close();

        return pages;
    }

    /**
     * Reads pages from a website unformated  and adds them to a list
     *
     * @param url The input url to read the pages from
     * @return The created pages from this website
     * @throws IOException
     */
    public static List<String> readBookUnformattedFromURL(String url) throws IOException
    {
        URL realURL = new URL(url);
        InputStream is;
        BufferedReader reader;

        is = realURL.openStream();
        reader = new BufferedReader(new InputStreamReader(is));

        return readUnformattedBook(reader);
    }
}
