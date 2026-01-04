package com.pos.base.ui.component;

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

@Getter
public class ChipGroup extends HorizontalLayout
        implements KeyNotifier, Focusable<ChipGroup> {

    private String selected;
    private final Map<String, Button> chips = new LinkedHashMap<>();
    private Consumer<String> selectionListener;

    public ChipGroup(List<String> topics) {
        addClassName("chip-group");
        setSpacing(true);

        topics.forEach(this::addChip);
        // Default selection
        if (!topics.isEmpty()) {
            select(topics.getFirst());
        }

        addKeyDownListener(Key.ARROW_RIGHT, e -> moveSelection(1));
        addKeyDownListener(Key.ARROW_LEFT, e -> moveSelection(-1));

        getElement().setAttribute("role", "tablist");
        getElement().setAttribute("aria-label", "Topic filter");
    }

    private void addChip(String label) {
        Button chip = new Button(label);
        chip.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        chip.addClassName("chip");

        chip.getElement().setAttribute("role", "tab");
        chip.getElement().setAttribute("aria-selected", "false");

        chip.addClickListener(e -> select(label));
        chips.put(label, chip);
        add(chip);
    }

    private void select(String label) {
        if (!chips.containsKey(label)) return;

        chips.forEach((key, chip) -> {
            chip.removeClassName("selected");
            chip.getElement().setAttribute("aria-selected", "false");
            chip.setTabIndex(-1);
        });

        Button active = chips.get(label);
        active.addClassName("selected");
        active.getElement().setAttribute("aria-selected", "true");
        active.setTabIndex(0);
        active.focus();

        selected = label;
        if (selectionListener != null) {
            selectionListener.accept(label);
        }
    }

    public void addSelectionListener(Consumer<String> listener) {
        this.selectionListener = listener;
    }

    public void moveSelection(int delta) {
        List<String> keys = new ArrayList<>(chips.keySet());
        int index = keys.indexOf(selected);

        int nextIndex = Math.floorMod(index + delta, keys.size());
        select(keys.get(nextIndex));
    }


}
