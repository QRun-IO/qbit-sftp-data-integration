# qbit-sftp-data-integration

SFTP file transfer and processing for QQQ applications.

**For:** QQQ developers who need to import data from or export data to SFTP servers  
**Status:** Stable

## Why This Exists

Enterprise integrations often rely on file drops. Partners send CSV files to an SFTP server. Nightly jobs export reports to remote locations. Building this means handling connections, parsing files, tracking what's been processed, and managing errors.

This QBit provides SFTP connectivity with file polling, parsing, and processing built in. Configure a connection, define file formats, and let QQQ handle the rest.

## Features

- **SFTP Connectivity** - Connect with password or key-based authentication
- **File Polling** - Scheduled checks for new files
- **Format Parsing** - CSV, fixed-width, and delimited file support
- **Record Mapping** - Map file columns to entity fields
- **Processing Tracking** - Track which files have been processed
- **Error Handling** - Move failed files to error directories

## Quick Start

### Prerequisites

- QQQ application (v0.35.0+)
- Database backend configured
- SFTP server credentials

### Installation

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.kingsrook.qbits</groupId>
    <artifactId>qbit-sftp-data-integration</artifactId>
    <version>0.3.0</version>
</dependency>
```

### Register the QBit

```java
public class AppMetaProvider extends QMetaProvider {
    @Override
    public void configure(QInstance qInstance) {
        new SftpDataIntegrationQBit().configure(qInstance);
    }
}
```

### Configure an SFTP Connection

```java
new QSftpConnectionMetaData()
    .withName("partnerSftp")
    .withHost("sftp.partner.com")
    .withPort(22)
    .withUsername("integration")
    .withPassword("${SFTP_PASSWORD}");  // From environment
```

## Usage

### Import Files

```java
new QSftpImportMetaData()
    .withName("dailyOrderImport")
    .withConnection("partnerSftp")
    .withRemotePath("/outbound/orders/")
    .withFilePattern("orders_*.csv")
    .withTargetTable("order")
    .withMapping(new QFieldMapping()
        .withColumn("order_id", "orderNumber")
        .withColumn("customer_email", "customerEmail")
        .withColumn("total", "orderTotal"));
```

### Export Files

```java
new QSftpExportMetaData()
    .withName("nightlyInventoryExport")
    .withConnection("partnerSftp")
    .withRemotePath("/inbound/inventory/")
    .withFileNamePattern("inventory_{date}.csv")
    .withSourceTable("product")
    .withFilter(new QQueryFilter()
        .withCriteria("stockLevel", Operator.LESS_THAN, 10));
```

### Scheduled Polling

```java
new QSftpImportMetaData()
    .withName("hourlyUpdates")
    .withSchedule("0 0 * * * *")  // Every hour
    .withConnection("partnerSftp")
    .withRemotePath("/updates/");
```

### Key-Based Authentication

```java
new QSftpConnectionMetaData()
    .withName("secureSftp")
    .withHost("secure.example.com")
    .withUsername("service")
    .withPrivateKeyPath("/path/to/id_rsa")
    .withPrivateKeyPassphrase("${KEY_PASSPHRASE}");
```

## Configuration

The QBit creates these tables:

| Table | Purpose |
|-------|---------|
| `sftp_connection` | Connection configurations |
| `sftp_file_log` | Processed file history |
| `sftp_error_log` | Failed file details |

### Post-Processing

```java
new QSftpImportMetaData()
    .withName("orderImport")
    .withPostProcessAction(PostProcessAction.MOVE)
    .withProcessedPath("/archive/")
    .withErrorPath("/errors/");
```

## Project Status

Stable and production-ready.

### Roadmap

- S3 and Azure Blob support
- PGP encryption/decryption
- File validation rules

## Contributing

1. Fork the repository
2. Create a feature branch
3. Run tests: `mvn clean verify`
4. Submit a pull request

## License

Proprietary - QRun.IO
