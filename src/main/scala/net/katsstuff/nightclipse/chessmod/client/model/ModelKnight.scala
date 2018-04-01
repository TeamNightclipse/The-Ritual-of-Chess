package net.katsstuff.nightclipse.chessmod.client.model

import net.minecraft.client.model.{ModelBiped, ModelRenderer}
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
class ModelKnight extends ModelBiped {

  var left_arm_overlay: ModelRenderer = _
  var right_leg_overlay: ModelRenderer = _
  var right_arm_overlay: ModelRenderer = _
  var left_leg_overlay: ModelRenderer = _
  var torso_overlay: ModelRenderer = _

  this.textureWidth = 64
  this.textureHeight = 64
  this.bipedHeadwear = new ModelRenderer(this, 32, 0)
  this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F)
  this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F)
  this.bipedRightArm = new ModelRenderer(this, 40, 16)
  this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F)
  this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F)
  this.left_arm_overlay = new ModelRenderer(this, 48, 48)
  this.left_arm_overlay.setRotationPoint(5.0F, 2.0F, 0.0F)
  this.left_arm_overlay.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F)
  this.right_leg_overlay = new ModelRenderer(this, 0, 32)
  this.right_leg_overlay.setRotationPoint(-1.9F, 12.0F, 0.0F)
  this.right_leg_overlay.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F)
  this.bipedHead = new ModelRenderer(this, 0, 0)
  this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F)
  this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F)
  this.bipedRightLeg = new ModelRenderer(this, 0, 16)
  this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F)
  this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F)
  this.bipedBody = new ModelRenderer(this, 16, 16)
  this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F)
  this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F)
  this.left_leg_overlay = new ModelRenderer(this, 0, 48)
  this.left_leg_overlay.setRotationPoint(1.9F, 12.0F, 0.0F)
  this.left_leg_overlay.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F)
  this.torso_overlay = new ModelRenderer(this, 16, 32)
  this.torso_overlay.setRotationPoint(0.0F, 0.0F, 0.0F)
  this.torso_overlay.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F)
  this.right_arm_overlay = new ModelRenderer(this, 40, 32)
  this.right_arm_overlay.setRotationPoint(-5.0F, 2.0F, 0.0F)
  this.right_arm_overlay.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F)
  this.bipedLeftArm = new ModelRenderer(this, 32, 48)
  this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F)
  this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F)
  this.bipedLeftLeg = new ModelRenderer(this, 16, 48)
  this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F)
  this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F)

  override def render(entity: Entity, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float): Unit = {
    super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
    GlStateManager.pushMatrix()
    if (this.isChild) {
      GlStateManager.scale(0.5F, 0.5F, 0.5F)
      GlStateManager.translate(0.0F, 24.0F * scale, 0.0F)
      this.left_leg_overlay.render(scale)
      this.right_leg_overlay.render(scale)
      this.left_arm_overlay.render(scale)
      this.right_arm_overlay.render(scale)
      this.torso_overlay.render(scale)
    }
    else {
      if (entity.isSneaking) GlStateManager.translate(0.0F, 0.2F, 0.0F)
      this.left_leg_overlay.render(scale)
      this.right_leg_overlay.render(scale)
      this.left_arm_overlay.render(scale)
      this.right_arm_overlay.render(scale)
      this.torso_overlay.render(scale)
    }
    GlStateManager.popMatrix()
  }

  override def setRotationAngles(limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scaleFactor: Float, entityIn: Entity): Unit = {
    def copyModelAngles(source: ModelRenderer, dest: ModelRenderer) {
      dest.rotateAngleX = source.rotateAngleX
      dest.rotateAngleY = source.rotateAngleY
      dest.rotateAngleZ = source.rotateAngleZ
      dest.rotationPointX = source.rotationPointX
      dest.rotationPointY = source.rotationPointY
      dest.rotationPointZ = source.rotationPointZ
    }

    super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn)
    copyModelAngles(this.bipedLeftLeg, this.left_leg_overlay)
    copyModelAngles(this.bipedRightLeg, this.right_leg_overlay)
    copyModelAngles(this.bipedLeftArm, this.left_arm_overlay)
    copyModelAngles(this.bipedRightArm, this.right_arm_overlay)
    copyModelAngles(this.bipedBody, this.torso_overlay)
  }

  override def setVisible(visible: Boolean): Unit = {
    super.setVisible(visible)
    this.left_arm_overlay.showModel = visible
    this.right_arm_overlay.showModel = visible
    this.left_leg_overlay.showModel = visible
    this.right_leg_overlay.showModel = visible
    this.torso_overlay.showModel = visible
  }
}
