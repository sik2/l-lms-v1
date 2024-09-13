'use client'

import { Button } from '@/components/ui/button'

export default function Login() {
    const getKakaoLoginUrl = () => {
        return `http://localhost:8090/member/socialLogin/kakao?redirectUrl=${encodeURIComponent(
            'http://localhost:3000'
        )}/member/socialLoginCallback?provierTypeCode=kakao`
    }

    return (
        <div>
            <a href={getKakaoLoginUrl()}>
                <Button>카카오 로그인 하기 </Button>
            </a>
        </div>
    )
}
