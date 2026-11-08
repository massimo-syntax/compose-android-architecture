# Ktor REST Server

A simple Ktor-based REST server running on port **8080**.

## Running the Server

```bash
./gradlew run
```

The server will start and be available at `http://localhost:8080`.

## Accessing from Your Home Network

By default, the server is configured to listen on all network interfaces (`0.0.0.0`), so it can be reached from other devices on your local network.

### 1. Find Your Local IP Address

```bash
ifconfig | grep "inet "
# or
ip addr show | grep "inet " | grep -v 127.0.0.1

# or
ifconfig
# under
wlan0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet -> 192.168.8.101 <-  netmask 255.255.255.0  broadcast 192.168.8.255
        ...
```

Look for an address like `192.168.8.101`.

### 2. Access from Another Device

Open a browser or use `curl` from another device on the same network:

```
http://192.168.8.101:8080/health
```

### 3. Firewall (if needed)

If the server is not reachable from another device, your firewall may be blocking port 8080. Choose the section that matches your system.

---

#### Debian / Kali (nftables)

Modern Debian-based distributions (including Kali Linux) use **nftables** as the default firewall.

**Step 1 — Check your current ruleset:**
```bash
sudo nft list ruleset
```
If you see output like `table inet filter { ... }`, the filter table already exists. If the output is empty, you need to create it first.

**Step 2 — If the filter table does NOT exist, create it:**
```bash
# Create the table
sudo nft add table inet filter

# Create the input chain
sudo nft add chain inet filter input '{ type filter hook input priority 0; }'
```

**Step 3 — Add the rule to allow port 8080:**
```bash
sudo nft add rule inet filter input tcp dport 8080 accept
```

**Step 4 — Verify the rule is in place:**
```bash
sudo nft list ruleset
```
You should see `tcp dport 8080 accept` inside the `chain input` block.

**Optional — Make the rule persistent across reboots:**
```bash
# Save the current ruleset
sudo nft list ruleset | sudo tee /etc/nftables.conf

# Ensure nftables starts on boot
sudo systemctl enable nftables
sudo systemctl start nftables
```

---

#### Ubuntu (ufw)

Ubuntu uses **ufw** (Uncomplicated Firewall) by default.

**Step 1 — Check the status of ufw:**
```bash
sudo ufw status
```
If it says `inactive`, enable it first:
```bash
sudo ufw enable
```

**Step 2 — Allow incoming traffic on port 8080:**
```bash
sudo ufw allow 8080/tcp
```

**Step 3 — Verify the rule:**
```bash
sudo ufw status
```
You should see `8080/tcp` listed as `ALLOW`.

**Optional — Check ufw logs if something is still blocked:**
```bash
sudo ufw status verbose
sudo tail -f /var/log/ufw.log
```

## Configuration
Server settings are in `src/main/resources/application.yaml`:

```yaml
ktor:
  deployment:
    port: 8080
    host: 0.0.0.0          # Listen on all interfaces (home network access)
  application:
    modules:
      - com.example.SerializationKt.configureSerialization
      - com.example.RoutingKt.configureRouting
```

To restrict access to localhost only, change `host` to `127.0.0.1`.
