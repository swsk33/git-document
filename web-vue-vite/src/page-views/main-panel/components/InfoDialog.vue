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

<script>
export default {
	data() {
		return {
			frameShow: false
		};
	}
};
</script>

<style lang="scss">
.info-dialog {
	position: absolute;
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
		width: 42%;
		height: 56%;
		margin: 0 auto;
		top: 22%;
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
			height: 80px;
			line-height: 80px;
			text-align: center;
			margin-top: 2%;
		}

		.content {
			height: 60%;
			width: 90%;
			left: 5%;
			display: flex;
			flex-direction: column;
			align-items: center;
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
			height: 10%;
			margin-top: 3%;
			display: flex;
			align-items: center;
			justify-content: space-evenly;
		}
	}
}
</style>