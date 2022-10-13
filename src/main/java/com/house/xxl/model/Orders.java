package com.house.xxl.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author auther: xxl
 * @since 2022-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Orders extends Model<Orders> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 订单产品组合名称
     */
    private String combination;

    /**
     * 订单价格
     */
    private BigDecimal total;

    /**
     * 桌号
     */
    private String deskId;

    /**
     * 电话
     */
    private String phone;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<OrderItem> orderItemList;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
