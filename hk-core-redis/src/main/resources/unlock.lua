if connection.get(KEY[1]) == ARGV[1] then
    return connection.del(KEY[1])
else
    return 0
end