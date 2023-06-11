package net.minecraft.data.advancements;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class AdvancementProvider implements DataProvider {
   private final PackOutput.PathProvider pathProvider;
   private final List<AdvancementSubProvider> subProviders;
   private final CompletableFuture<HolderLookup.Provider> registries;
   @org.jetbrains.annotations.Nullable
   protected final net.minecraftforge.common.data.ExistingFileHelper existingFileHelper;

   /**
    * @deprecated Forge: Use the {@linkplain #AdvancementProvider(PackOutput, CompletableFuture, List, net.minecraftforge.common.data.ExistingFileHelper) existing file helper variant}
    */
   @Deprecated
   public AdvancementProvider(PackOutput p_256529_, CompletableFuture<HolderLookup.Provider> p_255722_, List<AdvancementSubProvider> p_255883_) {
      this(p_256529_, p_255722_, p_255883_, null);
   }

   public AdvancementProvider(PackOutput p_256529_, CompletableFuture<HolderLookup.Provider> p_255722_, List<AdvancementSubProvider> p_255883_, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper) {
      this.existingFileHelper = existingFileHelper;
      this.pathProvider = p_256529_.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
      this.subProviders = p_255883_;
      this.registries = p_255722_;
   }

   public CompletableFuture<?> run(CachedOutput p_254268_) {
      return this.registries.thenCompose((p_255484_) -> {
         Set<ResourceLocation> set = new HashSet<>();
         List<CompletableFuture<?>> list = new ArrayList<>();
         Consumer<Advancement> consumer = (p_253397_) -> {
            if (!set.add(p_253397_.getId())) {
               throw new IllegalStateException("Duplicate advancement " + p_253397_.getId());
            } else {
               Path path = this.pathProvider.json(p_253397_.getId());
               list.add(DataProvider.saveStable(p_254268_, p_253397_.deconstruct().serializeToJson(), path));
            }
         };

         // Forge: Allow mods to take control of advancement registration
         this.registerAdvancements(p_255484_, consumer, this.existingFileHelper);

         return CompletableFuture.allOf(list.toArray((p_253393_) -> {
            return new CompletableFuture[p_253393_];
         }));
      });
   }

   /**
    * Registers all {@linkplain Advancement advancements}.
    *
    * @param registries the registries available for lookup including all registries in {@link net.minecraft.core.registries.Registries}
    * @param saver a {@link Consumer} which saves any advancements provided
    * @param existingFileHelper the existing file helper to check for the existence of files like parent advancements
    * @see Advancement.Builder#save(Consumer, ResourceLocation, net.minecraftforge.common.data.ExistingFileHelper)
    */
   protected void registerAdvancements(HolderLookup.Provider registries, Consumer<Advancement> saver, net.minecraftforge.common.data.ExistingFileHelper existingFileHelper) {
      for (AdvancementSubProvider subProvider : this.subProviders) {
         subProvider.generate(registries, saver);
      }
   }

   public final String getName() {
      return "Advancements";
   }
}
