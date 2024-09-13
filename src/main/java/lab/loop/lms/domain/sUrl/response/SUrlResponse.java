package lab.loop.lms.domain.sUrl.response;

import lab.loop.lms.domain.sUrl.dto.SUrlDTO;
import lab.loop.lms.domain.sUrl.entity.SUrl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SUrlResponse {
    private SUrlDTO sUrlDTO;

    public SUrlResponse(SUrl sUrl) {
        this.sUrlDTO = new SUrlDTO(sUrl);
    }
}

