// 权限管理
import ACCESS_ENUM from '@/access/accessEnum'
import router from '@/router'
import store from '@/store'
import checkAccess from '@/access/checkAccess'

router.beforeEach(async (to, from, next) => {
	// 获取当前登录用户
	let loginUser = store.state.user.loginUser

	// 自动登录
	// 未登录过 -> 没有登录用户或登录用户的 userRole 不存在[执行了登录一定有 userRole],看做是否为首次登录
	if (!loginUser || !loginUser.user) {
		// 执行登录 -> await -> 等用户登录成功之后，再执行后续的代码
		await store.dispatch('user/getLoginUser')

		// 重新获取当前登录用户
		loginUser = store.state.user.loginUser
	}
	// 获取当前页面需要的权限
	const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN

	// 要跳转的页面需要权限【必须要登录或者管理员权限】
	if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
		// 如果未登录或 userRole 不存在
		if (!loginUser || !loginUser.userRole || loginUser.userRole === ACCESS_ENUM.NOT_LOGIN) {
			// 跳转到登录页面 获取完整路由 to.fullPath
			next(`/user/login?redirect=${to.fullPath}`)
			return
		}

		// 已经登录 -> 校验权限 -> 权限不足 -> 跳转到无权限页面
		if (!checkAccess(loginUser, needAccess)) {
			// 跳转到登陆忧
			next('noAuth')
			return
		}
	}
	next()
})
