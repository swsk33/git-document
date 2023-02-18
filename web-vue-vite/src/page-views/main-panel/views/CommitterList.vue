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

<script setup>
import { sendRequest, REQUEST_METHOD } from '../../../utils/request';
import { timestampToDateString } from '../../../utils/time-convert';
import { ElNotification } from 'element-plus';
import { computed, onBeforeMount, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

// 响应式变量
const loadingDone = ref(false);
const commits = ref([]);

// 计算属性
const getUpdateTime = computed(() => (timestamp) => {
	return timestampToDateString(timestamp);
});

onBeforeMount(async () => {
	// 拉取贡献者列表
	const response = await sendRequest('/api/anthology/get-all-commits/' + route.params.id, REQUEST_METHOD.GET);
	loadingDone.value = true;
	if (!response.success) {
		ElNotification({
			title: '失败',
			message: '获取贡献者列表失败！请联系后端开发者！',
			type: 'error',
			duration: 1000
		});
		return;
	}
	commits.value = response.data;
});
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