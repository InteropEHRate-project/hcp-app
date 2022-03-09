package eu.interopehrate.hcpapp.mvc.commands.index;

import eu.interopehrate.hcpapp.currentsession.CloudConnectionState;
import eu.interopehrate.hcpapp.currentsession.D2DConnectionState;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class IndexCommand {
    private String bluetoothConnectionInfoImage;
    private String bluetoothConnectionInfoImageSize;
    private D2DConnectionState connectionState;
    private CloudConnectionState cloudConnectionState;
    private IndexPatientDataCommand patientDataCommand;
    public static Boolean transmissionCompleted = false;

    @NotEmpty
    @NotNull
    private String qrCode;
    private String bucketName;
    private String hospitalID = "simonamaria";
    private String hcoHospital ="TUlJRkN6Q0NBM09nQXdJQkFnSVVUa3BNZGdZcDhTN1VXYjgwUFpsbVoxTDJUemd3RFFZSktv" +
            "WklodmNOQVFFTApCUUF3WVRFak1DRUdDZ21TSm9tVDhpeGtBUUVNRTJNdE1HTTNOVFZoT0RrM01EaGlNMlV4WXpJeEZUQVRC" +
            "Z05WCkJBTU1ERTFoYm1GblpXMWxiblJEUVRFak1DRUdBMVVFQ2d3YVJVcENRMEVnUTI5dWRHRnBibVZ5SUZGMWFXTnIKYzNSa" +
            "GNuUXdIaGNOTWpJd01qSTFNVEl5TVRReFdoY05NalF3TWpJMU1USXlNVFF4V2pBZE1Sc3dHUVlEVlFRRApEQkpvWldGc2RHaHZjb" +
            "WRoYm1sNllYUnBiMjR3Z2dJaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQ0R3QXdnZ0lLCkFvSUNBUURZLzdXa1B4UGc2OWlzdjJIYmV" +
            "BTGNVQlZROGI0RHdkN0ZOU2djdU8zR2RpTmpkL0V2UzkzKzlkSWgKKytobFN4MTFPYmxFNDhmc3FJa3hUVWFreFJsc3JIRVByUzFkeTh" +
            "FYXQ0dDFtR3JnWVpjOVpoMXpNcEtrS1F2bwo1d3dHNG43SmdVOWFoSUxNbDhyZTFObmZQVmllUEpyREpkOEEzOGQvbFFldWRVSGtoZUN3Y0" +
            "kwZnhCdUY5dkVFCnFTVUM0ZVdFUFJRNERBU2lMdWY4NGZzaDVEeDZEanROdE1EL20xRGJkb3NLbHR1V1JGZC9yOFhSbFdDQ3orbnIKSW4vcmd" +
            "VaE02bE5RQ1labnpOdGxub3NHTzlXeStWOFZydXZQdkpuRlRaSDVzMllTTXA3b0hML3hmVDZZRGRKcwpBbzJYSmVCRkxSU0hCeFFBZkRDMmJCNVB" +
            "EejEwYURRamZKbzBIRU9EODFUN01NYWM4KzJybjJxRE5mbmp0M3ZjCmhHSVdGZFlQOE9TVmpianRvVld0ajJha2dmWjJmR1NRbzVCVUpzL2VtS3F2NnZ" +
            "tc1BRN3ZmYTVwSi9QQzA2WEsKTzhldHdheXZGL0xzejN3a1oybE83UGkwTFVFYkVwdXdFVlp0emgwZEZNaG5VSDBJeW5hTFR0a2ZiTVdJYUlZYQppZUNXS3Z" +
            "jOU9ycTRsWEE3M2FkNFRRdG96MVp4ZSszQk1lams3cTZUNStldkJ4ZEtXTVQ5VUJ2RzdaODVLejdECnZpdG1hcVM4VGFLRHJhUHlmMDJFQTFUeW5wSkc3d2RERF" +
            "o5NEN5MFpTMjYzcGxsRzhjVlRNdHdPQVdHTStyVUcKdUVYUmZsSzhDdk0zVGt5ZnhBTHdYZVpKYSsyUTBzK1AvemVON1BldUFEc1ZTTTVxWXdJREFRQUJvMzh3ZlRBTQ" +
            "pCZ05WSFJNQkFmOEVBakFBTUI4R0ExVWRJd1FZTUJhQUZBcXhWR0xmOEFGb2MxODNFZzI1SHlpd2c5RHRNQjBHCkExVWRKUVFXTUJRR0NDc0dBUVVGQndNQ0JnZ3JCZ0VGQlFj" +
            "REJEQWRCZ05WSFE0RUZnUVUyUW1XbWdBY3d6dGsKTHhRNFY2YzVYazltVFdNd0RnWURWUjBQQVFIL0JBUURBZ1hnTUEwR0NTcUdTSWIzRFFFQkN3VUFBNElCZ1FDawo1QTROWThmM2" +
            "V5U1lTQWJPTW5aODl5OE5FS2tta3VBQ0IwVlZ0NnZIbmxUR3ZjSHlBOFpVSTR5WEh5YVNkOUVxClExbWQzVEVFZ0N4bWpjL1kva2czWW1qR3RTSXV3Z1hjUVlBY0dRbCtvUW1LVmoyZkEx" +
            "NDd6SWNYbWpTZUlLZGQKd0xzSDA2dTZMV0wrdlVhN1VVbVFDclZuODRyMzM4Uyt4blkxM0M2b0VWc2VvWXBpcTNZV05nVVltNG1yWVRPeApxbFphUks1aVE1eVRnSVBsZkQvS1QzYXFpekswY" +
            "VloVTdUKzZNbjZuNU1oYVZZdUFJK3UrVXhRU05KNlVTdnpYCldkRXdiM2hjb2Y0NVdSVXp5WnBlejRKdTBGOGpJdlhuUWszUjQ5SmxXUTVtUG5UbTJhQTdGM3FnQnVqZGVjbzUKU2Q2alZkZzRNY" +
            "UZpeWk5RnlZVlkwd2V0cFVyemFTeDg0ZUhRTjVQS05taVEzYlo4bW81aklpbm9nS05scWd4SgpoYzFFSFQrT1hsRnFFVFhsa0dYNjJpeFduSExXUkVZUWRrSFY4Y0Nqc2t4ZWRhdFBUclV2WUpnU09H" +
            "ZGFGUktRCnBUNCsrNW15d0Y5WFExbWVhbU4yRXZqUWtobWZjSlY4TUlPcHoyb2RPeEEwYnBzV3NBdjFsWndSd2JRbTdJbz0=";
    private String hcpName = "Ion Popescu";
}
