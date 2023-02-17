<template>
	<div class="info-dialog" v-if="frameShow">
		<div class="frame">
			<div class="title">
				<slot name="title"></slot>
			</div>
			<div class="content">
				<slot name="content"></slot>
			</div>
			<div class="button-box">
				<slot name="button-box"></slot>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref } from 'vue';

let frameShow = ref(false);
</script>

<style lang="scss">
// 定义窗口宽高
$frame-width: 40vw;
$frame-height: 60vh;

// 根据窗口宽获得长度（百分比以小数表示）
@function getByWidthPercent($percent) {
	@return #{$frame-width * $percent};
}

// 根据窗口高获得长度（百分比以小数表示）
@function getByHeightPercent($percent) {
	@return #{$frame-height * $percent};
}

.info-dialog {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(255, 255, 255, 0.75);
	z-index: 99;

	@keyframes frameLayout {
		from {
			transform: scale(0);
		}

		to {
			transform: scale(1);
		}
	}

	.frame {
		position: relative;
		width: $frame-width;
		height: $frame-height;
		margin: 0 auto;
		top: calc(50% - #{$frame-height} / 2);
		background-color: #ddd8ff;
		border-radius: 6px;
		border: 1px #ff319e solid;
		animation: frameLayout;
		animation-duration: 0.3s;
		animation-timing-function: ease-out;

		.title, .content, .button-box {
			position: relative;
			box-sizing: border-box;
		}

		.title {
			font-size: 26px;
			height: getByHeightPercent(0.2);
			line-height: getByHeightPercent(0.2);
			text-align: center;
		}

		.content {
			height: getByHeightPercent(0.6);
			width: getByWidthPercent(0.95);
			left: getByWidthPercent(0.025);
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: space-evenly;
			overflow-x: hidden;
			overflow-y: auto;

			// 设定滚动条整体
			&::-webkit-scrollbar {
				width: 5px;
			}

			// 设定滚动条滑块
			&::-webkit-scrollbar-thumb {
				border-radius: 10px;
				background: rgba(0, 0, 0, 0.2);
			}

			// 设定外层轨道滚动槽
			&::-webkit-scrollbar-track {
				border-radius: 5px;
				background: rgba(0, 0, 0, 0.1);
			}
		}

		.button-box {
			height: getByHeightPercent(0.2);
			display: flex;
			align-items: center;
			justify-content: space-evenly;
		}
	}
}
</style>