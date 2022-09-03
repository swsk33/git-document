<template>
	<!-- 贡献者列表 -->
	<div class="committer-list" v-loading="!loadingDone" element-loading-text="正在拉取贡献者数据...">
		<el-table class="table" :data="commits" row-class-name="commit-row" empty-text="暂无贡献" border>
			<el-table-column label="贡献者" width="300" :resizable="false" align="center">
				<template #default="scope">
					<div class="committer-info">
						<img class="avatar" :src="scope.row.committer.avatar" alt="无法显示">
						<div class="nickname">{{ scope.row.committer.nickname }}</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="贡献时间" #default="scope" align="center" width="160">
				{{ getUpdateTime(scope.row.timestamp) }}
			</el-table-column>
			<el-table-column #default="scope" label="标注" align="center">
				<el-tooltip :content="scope.row.message" placement="top">
					{{ scope.row.message }}
				</el-tooltip>
			</el-table-column>
		</el-table>
	</div>
</template>

<script>
import { sendRequest, REQUEST_METHOD } from '../../../utils/request.js';
import { timestampToDateString } from '../../../utils/time-convert.js';
import { ElNotification } from 'element-plus';

export default {
	data() {
		return {
			loadingDone: false,
			commits: []
		};
	},
	computed: {
		getUpdateTime() {
			return (timestamp) => {
				return timestampToDateString(timestamp);
			};
		}
	},
	async created() {
		// 拉取贡献者列表
		const response = await sendRequest('/api/anthology/get-all-commits/' + this.$route.params.id, REQUEST_METHOD.GET);
		this.loadingDone = true;
		if (!response.success) {
			ElNotification({
				title: '失败',
				message: '获取贡献者列表失败！请联系后端开发者！',
				type: 'error',
				duration: 1000
			});
			return;
		}
		this.commits = response.data;
	}
};
</script>

<style lang="scss" scoped>
.committer-list {
	.table {
		position: relative;
		width: 96%;
		left: 2%;
		top: 2%;
		border-radius: 5px;
		border: 1px #4f4fff solid;

		.commit-row {
			.committer-info {
				display: flex;
				justify-content: space-evenly;
				align-items: center;

				.avatar {
					border: blue 1px solid;
					border-radius: 50%;
					max-width: 45px;
					max-height: 45px;
				}

			}
		}
	}
}
</style>