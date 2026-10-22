package fr.crownfall.mod;

import com.mojang.serialization.MapCodec;
import fr.crownfall.api.BlockLocation;
import fr.crownfall.api.Crownfall;
import fr.crownfall.api.event.CapturePointPlacedEvent;
import fr.crownfall.api.event.CapturePointRemovedEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Bloc de point de contrôle : le mod le fournit, le plugin décide de ce qu'il vaut. */
public class CapturePointBlock extends Block {

    public static final MapCodec<CapturePointBlock> CODEC = simpleCodec(CapturePointBlock::new);

    public CapturePointBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide() && placer instanceof Player player) {
            Crownfall.bridge().publish(new CapturePointPlacedEvent(location(level, pos), player.getUUID()));
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide() && !state.is(newState.getBlock())) {
            Crownfall.bridge().publish(new CapturePointRemovedEvent(location(level, pos)));
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private static BlockLocation location(Level level, BlockPos pos) {
        return new BlockLocation(level.dimension().location().toString(), pos.getX(), pos.getY(), pos.getZ());
    }
}
