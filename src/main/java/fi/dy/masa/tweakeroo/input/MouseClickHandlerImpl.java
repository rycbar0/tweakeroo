package fi.dy.masa.tweakeroo.input;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import fi.dy.masa.malilib.gui.util.GuiUtils;
import fi.dy.masa.malilib.input.MouseClickHandler;
import fi.dy.masa.malilib.util.game.wrap.GameUtils;
import fi.dy.masa.malilib.util.game.wrap.ItemWrap;
import fi.dy.masa.malilib.util.position.PositionUtils;
import fi.dy.masa.tweakeroo.config.FeatureToggle;

public class MouseClickHandlerImpl implements MouseClickHandler
{
    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int mouseButton, boolean buttonState)
    {
        EntityPlayerSP player = GameUtils.getClientPlayer();
        WorldClient world = GameUtils.getClientWorld();
        RayTraceResult hitResult = GameUtils.getHitResult();

        if (GuiUtils.getCurrentScreen() == null && player != null && player.capabilities.isCreativeMode &&
            buttonState && mouseButton == GameUtils.getClient().gameSettings.keyBindUseItem.getKeyCode() + 100 &&
            FeatureToggle.TWEAK_ANGEL_BLOCK.getBooleanValue() &&
            hitResult != null && hitResult.typeOfHit == RayTraceResult.Type.MISS)
        {
            BlockPos posFront = PositionUtils.getPositionInfrontOfEntity(player);

            if (world.isAirBlock(posFront))
            {
                EnumFacing facing = PositionUtils.getClosestLookingDirection(player).getOpposite();
                Vec3d hitVec = PositionUtils.getHitVecCenter(posFront, facing);
                ItemStack stack = player.getHeldItemMainhand();

                if (ItemWrap.notEmpty(stack) && stack.getItem() instanceof ItemBlock)
                {
                    GameUtils.getInteractionManager().processRightClickBlock(player, world, posFront, facing, hitVec, EnumHand.MAIN_HAND);
                    return true;
                }

                stack = player.getHeldItemOffhand();

                if (ItemWrap.notEmpty(stack) && stack.getItem() instanceof ItemBlock)
                {
                    GameUtils.getInteractionManager().processRightClickBlock(player, world, posFront, facing, hitVec, EnumHand.OFF_HAND);
                    return true;
                }
            }
        }

        return false;
    }
}
