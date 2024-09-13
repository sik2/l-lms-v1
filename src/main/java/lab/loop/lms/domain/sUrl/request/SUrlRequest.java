package lab.loop.lms.domain.sUrl.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class SUrlRequest {

    @Getter
    @Setter
    public static class CreateRequest {
        @NotNull(message = "URL은 필수입니다.")
        public String originUrl;

        @Min(value = 0)
        private Long redirectCount = 0L;

        private Boolean isActive = true;

        public LocalDateTime createDate;
    }

    @Getter
    @Setter
    public static class ModifyRequest {
        @NotNull(message = "URL은 필수입니다.")
        public String originUrl;

        public Long redirectCount;

        public Boolean isActive;

        public LocalDateTime modifyDate;
    }

    @Getter
    @Setter
    public static class DeleteRequest {

        public Long id;

        public Long redirectCount;

        public Boolean isActive;

        public LocalDateTime createDate;
    }
}
