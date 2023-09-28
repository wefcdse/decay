package net.iung;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class RuinsBlock extends Block {
    public static final BooleanProperty CHARGED = BooleanProperty.of("charged");

    public RuinsBlock(Settings settings) {

        super(settings.ticksRandomly());
        setDefaultState(getDefaultState().with(CHARGED, false));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGED);
    }

//    @Override
//    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
//        world.setBlockState(pos, state.with(CHARGED, true));
//        return ActionResult.SUCCESS;
//    }


    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        {
            var move_x = random.nextBetween(-1, 1);
            var move_y = random.nextBetween(-1, 1);
            var move_z = random.nextBetween(-1, 1);
            var pos1 = pos.add(move_x, move_y, move_z);
            BlockState target_block = world.getBlockState(pos1);
            if (!target_block.isAir()&&!target_block.isOf(Decay.COPY_BLOCK)) {
                world.setBlockState(pos1, state);
            }
        }
        {
            var move_x = random.nextBetween(-5, 5);
            var move_y = random.nextBetween(-5, 5);
            var move_z = random.nextBetween(-5, 5);
            var pos1 = pos.add(move_x, move_y, move_z);
            BlockState target_block = world.getBlockState(pos1);
            if (!target_block.isAir()) {
                world.setBlockState(pos1, state);
            }
        }

//        super.randomTick(state, world, pos, random);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (state.get(CHARGED)) {
            entity.damage(DamageSource.MAGIC, 5.0F);
        } else {
            if (!entity.isSneaking()) {
                entity.damage(DamageSource.MAGIC, 1.0F);
            }
        }
    }
}
