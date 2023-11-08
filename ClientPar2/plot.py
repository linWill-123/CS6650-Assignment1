import matplotlib.pyplot as plt

# Data
servers = ['Java', 'Java', 'Java', 'Go', 'Go', 'Go']
numThreadGroups = [10, 20, 30, 10, 20, 30]
wall_time = [175, 337, 499, 177, 340, 507]
# throughputs = [4122, 6040 ,7831, 10100,14731,19770]
throughputs = [1154,1192,1206, 1141,1182,1187]

# Separate Java and Go data
java_times = [wall_time[i] for i, server in enumerate(servers) if server == 'Java']
go_times = [wall_time[i] for i, server in enumerate(servers) if server == 'Go']

java_throughputs = [throughputs[i] for i, server in enumerate(servers) if server == 'Java']
go_throughputs = [throughputs[i] for i, server in enumerate(servers) if server == 'Go']

# Unique values of numThreadGroups
thread_groups_unique = sorted(list(set(numThreadGroups)))

# Plotting
plt.figure(figsize=(10,6))
# plt.plot(thread_groups_unique, java_times, marker='o', label='Java', color='blue')
# plt.plot(thread_groups_unique, go_times, marker='o', label='Go', color='red')
# plt.title("Wall time vs numThreadGroups for Different Servers")
# plt.xlabel("numThreadGroups")
# plt.ylabel("Wall time (seconds)")
plt.plot(thread_groups_unique, java_throughputs, marker='o', label='Java', color='blue')
plt.plot(thread_groups_unique, go_throughputs, marker='o', label='Go', color='red')
plt.title("Throughputs vs numThreadGroups for Different Servers")
plt.xlabel("numThreadGroups")
plt.ylabel("throughputs (requests/sec)")
plt.legend()
plt.grid(True)
plt.show()
