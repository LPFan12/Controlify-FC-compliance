package dev.isxander.controlify.controller.input.mapping;

import dev.isxander.controlify.controller.input.ControllerState;
import dev.isxander.controlify.controller.input.DeadzoneGroup;
import dev.isxander.controlify.controller.input.ModifiableControllerState;
import dev.isxander.controlify.controller.impl.ControllerStateImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public record UserGamepadMapping(
        List<MappingEntry> mappings,
        LinkedHashMap<ResourceLocation, DeadzoneGroup> deadzones
) implements GamepadMapping {
    @Override
    public ControllerState mapJoystick(ControllerState state) {
        if (mappings.isEmpty()) {
            return state;
        }

        ModifiableControllerState newState = new ControllerStateImpl();

        for (MappingEntry mapping : mappings) {
            mapping.apply(state, newState);
        }

        return newState;
    }

    public static final UserGamepadMapping NO_MAPPING = new Builder().build();

    public static class Builder {
        private final List<MappingEntry> mappings = new ArrayList<>();
        private final LinkedHashMap<ResourceLocation, DeadzoneGroup> deadzones = new LinkedHashMap<>();

        public Builder putMapping(MappingEntry mapping) {
            if (mapping == null)
                return this;

            mappings.add(mapping);
            return this;
        }

        public Builder putDeadzoneGroups(Iterable<DeadzoneGroup> deadzoneGroup) {
            for (DeadzoneGroup group : deadzoneGroup) {
                this.deadzones.put(group.name(), group);
            }
            return this;
        }

        public UserGamepadMapping build() {
            return new UserGamepadMapping(mappings, deadzones);
        }
    }
}
