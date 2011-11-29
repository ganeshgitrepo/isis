/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.examples.onlinedemo.objstore.dflt.demo;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.clock.Clock;
import org.apache.isis.applib.value.Date;
import org.apache.isis.examples.onlinedemo.dom.demo.DemoFixtures;
import org.apache.isis.examples.onlinedemo.dom.items.Categories;
import org.apache.isis.examples.onlinedemo.dom.items.Category;
import org.apache.isis.examples.onlinedemo.dom.items.ToDoItem;
import org.apache.isis.examples.onlinedemo.dom.items.ToDoItems;

public class DemoFixturesDefault extends AbstractFactoryAndRepository implements DemoFixtures {

    // {{ Id, iconName
    @Override
    public String getId() {
        return "fixtures";
    }

    public String iconName() {
        return "Fixtures";
    }
    // }}

    @Override
    public List<ToDoItem> resetDemoFixtures() {
        Category domesticCategory = findOrCreateCategory("Domestic");
        Category professionalCategory = findOrCreateCategory("Professional");
        
        removeAllToDosForCurrentUser();
        
        createToDoItemForCurrentUser("Buy milk", domesticCategory, daysFromToday(0));
        createToDoItemForCurrentUser("Buy stamps", domesticCategory, daysFromToday(0));
        createToDoItemForCurrentUser("Pick up laundry", domesticCategory, daysFromToday(6));
        createToDoItemForCurrentUser("Write blog post", professionalCategory, null);
        createToDoItemForCurrentUser("Organize brown bag", professionalCategory, daysFromToday(14));
        
        getContainer().flush();
        
        return toDoItems.allToDos();
    }

    // {{ helpers
    private Category findOrCreateCategory(String description) {
        Category category = categories.find(description);
        if(category != null) {
            return category;
        }
        return categories.newCategory(description);
    }

    private void removeAllToDosForCurrentUser() {
        final List<ToDoItem> allToDos = toDoItems.allToDos();
        for (ToDoItem toDoItem : allToDos) {
            getContainer().remove(toDoItem);
        }
    }

    private ToDoItem createToDoItemForCurrentUser(String description, Category category, Date dueBy) {
        return toDoItems.newToDo(description, category, dueBy);
    }

    private static Date daysFromToday(int i) {
        final Date date = new Date(Clock.getTimeAsDateTime());
        date.add(0, 0, i);
        return date;
    }
    // }}


    // {{ injected: Categories
    private Categories categories;

    public void setCategories(final Categories categories) {
        this.categories = categories;
    }
    // }}


    // {{ injected: ToDoItems
    private ToDoItems toDoItems;
    public void setToDoItems(ToDoItems toDoItems) {
        this.toDoItems = toDoItems;
    }
    // }}
    

}