<template>
	<view>
		<view class="nav">
			<!-- 左侧 -->
			<view class="nav-left">
				<scroll-view scroll-y scroll-with-animation :scrollTop="scrollLeftTop"
					:style="[{height: containerHeight + 'px'}]">
					<!-- TODO: 可自行修改样式，绑定事件等 -->
					<view class="nav-left-item" v-for="(item,index) in categoryList"
						@click="handleClickCate(item,index)" :key="index"
						:style="[index==activeIndex?activeStyle:null]">
						{{item[label]}}
					</view>
				</scroll-view>
			</view>

			<!-- 右侧 -->
			<view class="nav-right">
				<scroll-view @scroll="scrollRight" :scroll-top="scrollTop" scroll-y :height="containerHeight"
					:style="[{height: containerHeight + 'px'}]" scroll-with-animation>
					<index-list-component :sticky="sticky" :zIndex="zIndex" :offsetTop="stickyOffsetTop"
						@activeIndexChange="handleActiveIndexChange" ref="indexListRef"
						:defaultActiveIndex="defaultActive" :scrollTop="scrollRightTop" :index-list="indexList"
						@changeScrollTop="handleChangeScrollTop">
						<view v-for="(item, index) in categoryList" :key="index">
							<index-anchor :customStyle="{right: 'auto', left: 'auto'}" :index="item.name" />
							<!-- TODO: 可自行修改样式，绑定事件等 -->
							<view class="nav-right-item" v-for="(subItem, subItemIndex) in item.subCategoryList"
								:key="subItemIndex" @click="handleClickSub(subItem)">
								<!-- <image class="item-img" mode="scaleToFill" :src="subItem[imgSrc]" /> -->
								<img class="item-img" :src="subItem.imgSrc" />
								<!-- <view class="item-label">{{subItem.label}}</view> -->
								<view class="good__desc" style="flex: 1;">
									<b class="item-label">{{subItem.label}}</b>
									<p style='font-size: 24rpx;color: #999;margin: 6rpx 0;'>商品配置材料简述</p>
									<view class="r-flex-3">
										<text style='color: #f01414;font-size: 32rpx;'>￥37.00</text>
										<wmNumberBox destination="body-btn" model="2" @change="numberBoxClick">
										</wmNumberBox>
									</view>
								</view>
							</view>
						</view>
					</index-list-component>
				</scroll-view>
			</view>
		</view>
		<view id="body-btn">
			<goods-nav></goods-nav>
		</view>
	</view>
</template>

<script>
	import IndexListComponent from './index-list.vue'
	import IndexAnchor from './index-anchor.vue'
	/**
	 * categoryList 类目索引列表
	 * @description 通过点击左侧索引到达右侧内容位置，参考了uView的<IndexList>所以列表组件
	 * @tutorial 参考了 https://www.uviewui.com/components/indexList.html
	 * @property {Number} height 当前列表的高度，若不设置默认为屏幕高度 
	 * @property {Boolean} sticky 是否开启锚点自动吸顶（默认true）
	 * @property {Number String} offsetTop 锚点自动吸顶时与顶部的距离（默认0）
	 * @property {Number} defaultActive 默认选中的index值
	 * @property {Number String} zIndex 锚点吸顶时的层级（默认965）
	 * @property {Array} categoryList 内容列表，结构为 {name: 'xx', subCategoryList: []}
	 * @property {String} label 内容列表中label对应的key名字，默认为 'name'
	 * @property {String} activeTextColor 选中时文字颜色
	 * @property {String} activeBackgroundColor 选中时背景色
	 * @event {Function} categoryMainClick 选中左侧索引时触发
	 * @event {Function} categorySubClick 选中右侧内容项时触发
	 * @example <category-list :height="400" :defaultActive="2" :offsetTop="200" :categoryList="categoryList" :subCategoryList="subCategoryList" @categoryMainClick="categoryMainClick"
		 @categorySubClick="categorySubClick"></category-list>
	 */
	import wmNumberBox from "@/common/wm-numberBox_1.2/components/wm-numberBox/wm-number-box.vue"
	import goodsNav from '@/components/goods-nav/goods-nav.vue'
	export default {
		name: "category",

		data() {
			return {
				options: [{
					icon: 'headphones',
					text: '客服'
				}, {
					icon: 'shop',
					text: '店铺',
					info: 2,
					infoBackgroundColor: '#007aff',
					infoColor: "red"
				}, {
					icon: 'cart',
					text: '购物车',
					info: 2
				}],
				buttonGroup: [{
						text: '加入购物车',
						backgroundColor: '#ff0000',
						color: '#fff'
					},
					{
						text: '立即购买',
						backgroundColor: '#ffa200',
						color: '#fff'
					}
				],
				containerHeight: 0, // 记录height
				scrollTop: 0,
				scrollLeftTop: 0,
				scrollRightTop: 0,
				scrollHeight: 0,
				activeStyle: {
					color: this.activeTextColor,
					backgroundColor: this.activeBackgroundColor
				},
				activeIndex: -1,
				sideBarAnchorsRect: [],
			}
		},
		props: {
			height: {
				type: Number,
				default: 0
			},
			sticky: {
				type: Boolean,
				default: true
			},
			offsetTop: {
				type: Number,
				default: 0
			},
			zIndex: {
				type: Number,
				default: 965
			},

			defaultActive: {
				type: Number,
				default: 0
			},
			//主分类List
			categoryList: {
				type: Array,
				default: () => {
					return null;
				}
			},
			activeTextColor: {
				type: String,
				default: '#2979ff'
			},
			activeBackgroundColor: {
				type: String,
				default: '#eee'
			},
			label: {
				type: String,
				default: 'name'
			},
			imgSrc: {
				type: String,
				default: 'logo'
			},
			//主分类点击事件
			categoryMainClick: {},
		},
		computed: {
			indexList() {
				return this.categoryList.map((category) => category.name)
			},
			stickyOffsetTop() {
				let offsetTop = this.offsetTop || 0
				// #ifdef H5
				offsetTop = offsetTop + 44;
				// #endif
				return offsetTop
			}
		},

		methods: {
			onClick(e) {
				uni.showToast({
					title: `点击${e.content.text}`,
					icon: 'none'
				})
			},
			buttonClick(e) {
				console.log(e)
				this.options[2].info++
			},

			numberBoxClick(val) {
				console.log(val)
			},
			scroll(e) {
				this.scrollHeight = e.detail.scrollHeight;
			},
			scrollRight(e) {
				this.scrollRightTop = e.detail.scrollTop;
			},
			handleClickCate(category, index) {
				this.$emit('categoryMainClick', category)
				this.activeIndex = index;
				// 滑动右侧列表到指定index位置
				this.$refs.indexListRef.scrollToAnchor(index)
			},
			handleClickSub(subItem) {
				this.$emit('categorySubClick', subItem)
			},
			handleChangeScrollTop(wraperScrollTop) {
				this.scrollTop = wraperScrollTop
			},
			// 监听右侧滚动时index的变化
			handleActiveIndexChange(index) {
				if (this.activeIndex == index) return
				this.activeIndex = index
			},
			// 获取side-bar中每项的rect
			setSideBarAnchorsRect() {
				const sideBarAnchorList = []
				this.$getRect('.nav-left-item', true).then(rect => {
					this.sideBarAnchorsRect = rect.map(item => {
						return {
							height: item.height,
							top: item.top
						}
					})

				});
			},
			scrollToLeftAnchor(index) {
				const windowHeight = this.containerHeight
				const halfWindowHeight = windowHeight / 2
				let stickyOffsetTop = this.offsetTop
				// // #ifdef H5
				// stickyOffsetTop = this.offsetTop - 44;
				// // #endif
				// // #ifndef H5
				// stickyOffsetTop = this.offsetTop;
				// // #endif
				const targetIndexTop = this.sideBarAnchorsRect[index] && (this.sideBarAnchorsRect[index].top -
					stickyOffsetTop)
				if (targetIndexTop > halfWindowHeight) {
					this.scrollLeftTop = targetIndexTop - halfWindowHeight
				} else {
					this.scrollLeftTop = 0
				}
			}
		},

		mounted() {
			this.activeIndex = this.defaultActive
			uni.getSystemInfo({
				success: res => {
					const height = this.height
					const windowHeight = res.windowHeight // 可使用窗口高度
					if (height) {
						// 限制containerHeight的值，避免传入的height值过大而出现滚动条
						const remainHeight = windowHeight - this.stickyOffsetTop // 因设置了offsetTop后剩余的height值
						if (height > remainHeight) {
							this.containerHeight = remainHeight
						} else {
							this.containerHeight = height
						}
					} else {
						this.containerHeight = windowHeight
					}
				}
			})
			this.setSideBarAnchorsRect()
		},
		watch: {
			activeIndex(newValue, oldValue) {
				if (newValue != oldValue) {
					// 滑动左侧列表到指定位置
					this.scrollToLeftAnchor(newValue)
				}
			}
		},
		components: {
			IndexAnchor,
			IndexListComponent,
			wmNumberBox,
			goodsNav,
		}
	}
</script>

<style lang="scss" scoped>
	.nav {
		display: flex;
		width: 100%;
	}

	.nav-left {
		width: 30%;
	}

	.nav-left-item {
		height: 50px;
		border-right: solid 1px #E0E0E0;
		border-bottom: solid 1px #E0E0E0;
		font-size: 14px;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.nav-right {
		width: 70%;

		.sub-title {
			font-weight: bold;
			font-size: 14px;
		}
	}

	.nav-right-item {
		// text-align: center;
		padding: 5px;
		font-size: 13px;
		display: flex;
		margin-bottom: 10px;

		.item-img {
			width: 60px;
			height: 60px;
			margin-right: 10px;
		}

		.item-label {}
	}

	.active {
		color: #F24544;
	}

	.padding {
		height: var(--status-bar-height);
		width: 100%;
		top: 0;
		position: fixed;
		background-color: #F24544;
	}

	.line {
		height: 1px;
		width: 100%;
		background-color: #eee;
	}
</style>
